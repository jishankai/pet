<?php

class DbConnection extends CDbConnection
{
    public function ensureConnection()
    {
        try {
            set_error_handler(create_function('', "throw new Exception(); return true;"));
            $this->getPdoInstance()->query('SELECT 1');
        } catch (Exception $e) {
            $this->setActive(false);
            $this->setActive(true);
        }
        restore_error_handler();
        return $this;
    }
}
