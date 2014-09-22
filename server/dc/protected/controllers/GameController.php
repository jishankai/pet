<?

class GameController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
        );
    }

    public function action2048($aid)
    {
       $this->render('game/index'); 
    }
}

