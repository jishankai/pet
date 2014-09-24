<?

class GameController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
        );
    }

    public function actions()
    {
        return array(
            '2048'=>array(
                'class'=>'CViewAction',
                'basePath'=>'2048',
                'layout'=>FALSE,
            ),
        );
    }
}

