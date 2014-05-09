<?php
/**
 * ExportDataCommand class file.
 *
 * @author Qi Changhai <qi.changhai@adways.net>
 */
/**
 * ExportDataCommand is base class for command classed to export data from server into files which will be loaded by client.
 *
 * @author Qi Changhai <qi.changhai@adways.net>
 */
abstract class ExportDataCommand extends CConsoleCommand
{
    /**
     * @var Array The file format supported and their handler function.
     * The handler funciton shoud have signature like
     * <pre>
     * function foo($fh, $data);
     * </pre>
     */
    protected $formatHandler = array(
        'json' => 'exportAsJson',
    );

    /**
     * Returns the name of file.
     * @return String The name of file. Default to command name without the "export".
     */
    protected function fileName()
    {
        preg_match('/^(export)?(\w+)$/', $this->name, $matches);
        return $matches[2];
    }

    /**
     * Returns the data to be exported.
     * @return mixed The data to be exported.
     */
    abstract protected function getData();

    /**
     * The default action.
     * Load data and write into file.
     *
     * @param String $format The format of file. The default is 'json'.
     * @param String $path The path relative to parent path of basePath. The default is 'resources'.
     */
    public function actionIndex($format="json", $path="resources")
    {
        $path=Yii::app()->basePath . DIRECTORY_SEPARATOR . '..' . DIRECTORY_SEPARATOR . $path;
        $fh = fopen($path . DIRECTORY_SEPARATOR . $this->fileName() . '.' . $format, 'w');

        if(isset($this->formatHandler[$format])){
            $handler = $this->formatHandler[$format];
            $this->$handler($fh, $this->getData());
        }else{
            throw new CException(Yii::t('Command', 'The format {format} is not supported.', array('{format}' => $format)));
        }

        fclose($fh);
    }

    protected function exportAsJson($fh, $data)
    {
        return fwrite($fh, CJSON::encode($data));
    }

}
