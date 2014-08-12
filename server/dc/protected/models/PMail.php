<?php

class PMail extends Mail
{
    public static function create($usr_id, $fromer, $body, $is_system=TRUE)
    {
        $mail = new PMail;
        $mail->usr_id = $usr_id;
        $mail->from_id = $fromer->usr_id;
        $mail->body = $body;
        $mail->is_system = $is_system;
        $mail->name = $fromer->name;
        $mail->tx = $fromer->tx;
        $mail->gender = $fromer->gender;

        $mail->create_time = time();

        if ($mail->save()) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
}
