<?php

class AlipayController extends Controller
{
	public function actionGotoPay()
	{
		$alipay = Yii::app()->alipay;
    	// If starting a guaranteed payment, use AlipayGuaranteeRequest instead
		$request = new AlipayDirectRequest();
		$request->out_trade_no = "unique order number";
		$request->subject = "product name";
		$request->body = "product description";
		$request->total_fee = 100.00;
    	// Set other optional params if needed
		$form = $alipay->buildForm($request);
		echo $form;
		exit();
	}

	// Server side notification
	public function actionNotify() {
		$alipay = Yii::app()->alipay;
		if ($alipay->verifyNotify()) {
			$order_id = $_POST['out_trade_no'];
			$order_fee = $_POST['total_fee'];
			if($_POST['trade_status'] == 'TRADE_FINISHED' || $_POST['trade_status'] == 'TRADE_SUCCESS') {
				update_order_status($order_id, $order_fee, $_POST['trade_status']);
				echo "success";
			}
			else {
				echo "success";
			}
		} else {
			echo "fail";
			exit();
		}
	}

	//Redirect notification
	public function actionReturn() {
		$alipay = Yii::app()->alipay;
		if ($alipay->verifyReturn()) {
			$order_id = $_GET['out_trade_no'];
			$total_fee = $_GET['total_fee'];

			if($_GET['trade_status'] == 'TRADE_FINISHED' || $_GET['trade_status'] == 'TRADE_SUCCESS') {
				update_order_status($order_id, $total_fee, $_POST['trade_status']);
				$this->render('order_paid');
			}
			else {
				echo "trade_status=".$_GET['trade_status'];
			}
		} else {
			echo "fail";
			exit();
		}
	}

}
