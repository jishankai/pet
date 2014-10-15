<?php

class ItemController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId',
            /*
            array(
                'COutputCache + listApi',
                'duration' => 86400,
                'varyByParam' => array('code'),
            ),
            array(
                'COutputCache + infoApi',
                'duration' => 3600,
                'varyByParam' => array('item_id'),
            ),
             */
        );
    }

    /*
    public function actionListApi($code=0)
    {
        $r = Yii::app()->db->createCommand('SELECT item_id FROM dc_item')->queryColumn();
        if (isset($r)) {
            $tmp_code = md5($r);
        } else {
            $tmp_code = 0;
        }
        
        if ($code==$tmp_code) {
            $this->echoJsonData(array('is_update'=>FALSE));        
        } else {
            $this->echoJsonData(array(
                'is_update'=>TRUE,
                'item_ids'=>$r,
                'code' => $tmp_code,
            ));        
        }
    }

    public function actionInfoApi($item_id)
    {
        $item = Item::model()->findByPk($item_id);

        $this->echoJsonData($item);
    }
     */

    public function actionBuyApi($item_id, $num)
    {
        $itemList = Util::loadConfig('items');
        $item = $itemList[$item_id];

        if (isset($item)) {
            $user = User::model()->findByPk($this->usr_id);
            if ($user->gold-$item['price']*$num<0) {
                throw new PException('余额不足');
            } else {
                $transaction = Yii::app()->db->beginTransaction();
                try {
                    $user->gold-=$item['price']*$num;
                    $items = unserialize($user->items);
                    if (isset($items[$item_id])) {
                        $items[$item_id]+=$num;
                    } else {
                        $items[$item_id]=$num;
                    }
                    $user->items = serialize($items);
                    $user->saveAttributes(array('gold', 'items'));
                    $transaction->commit();
                } catch (Exception $e) {
                    $transaction->rollback();
                    throw $e;
                }

                $this->echoJsonData(array('user_gold'=>$user->gold));
            }

        } else {
            throw new PException('商品不存在');
        }
    }
}
