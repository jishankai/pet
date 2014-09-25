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
        $this->layout = FALSE;
        $this->render('2048');
    }
}

