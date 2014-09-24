<?

class GameController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
        );
    }

    public function action2048($aid)
    {
        $this->render('2048');
    }
}

