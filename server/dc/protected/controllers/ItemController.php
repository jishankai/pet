<?php

class ItemController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId',
        );
    }

    public function actionListApi($code)
    {
        $r = Yii::app()->db->createCommand('SELECT item_id FROM dc_item')->queryColumn();
        $tmp_code = md5($r);
        if ($code==$tmp_code) {
            $this->echoJsonData(array('is_update'=>FALSE));        
        } else {
            $this->echoJsonData(array(
                'is_update'=>TRUE,
                'item_ids'=>$r,
            ));        
        }
    }

    public function actionInfoApi($item_id)
    {
        $item = Item::model()->findByPk($item_id);

        $this->echoJsonData($item);
    }

    public function actionBuyApi($item_id, $num)
    {
        $item = Item::model()->findByPk($item_id);

        if (isset($item)) {
            $user = User::model()->findByPk($this->usr_id);
            if ($user->gold-$item->price*$num<0) {
                throw new PException('余额不足');
            } else {
                $user->gold-=$item->price*$num;
                $items = unserialize($user->items);
                if (isset($items[$item_id])) {
                    $items[$item_id]+=$num;
                } else {
                    $items[$item_id]=$num;
                }
                $user->items = serialize($items);
                $user->saveAttributes(array('gold', 'items'));

                $this->echoJsonData(array('user_gold'=>$user->gold));
            }

        } else {
            throw new PException('商品不存在');
        }
    }
}
