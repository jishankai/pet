<?php
/**
 * WebApplicationApiBehavior class file.
 *
 * @authro Qi Changhai <qi.changhai@adways.net>
 */
/**
 * WebApplicationApiBehavior add some event handlers to handle request and exceptions.
 *
 * @author Qi Changhai <qi.changhai@adways.net>
 */
class WebApplicationApiBehavior extends CBehavior
{
    /**
     * @var string the class of response.
     */
    public $responseClass;

    protected $response;

    /**
     * Returns the response object.
     * @return IResponse
     */
    public function getResponse()
    {
        if(!isset($this->response)){
            $responseClass = $this->responseClass;
            $this->response = new $responseClass();
        }
        return $this->response;
    }

    /**
     * Returns the events this behavior will handle.
     * @return array The events and their handlers this behavior will handle.
     */
    public function events()
    {
        return array(
            'onBeginRequest' => 'handleBeginRequest',
            'onEndRequest' => 'handleEndRequest',
            'onException' => 'handleException',
            'onError' => 'handleError',
        );
    }

    /**
     * Handle onBeginRequest event.
     * If the incoming request has been requested before, the cached response will be 
     * sent directly, to prevent re-request.
     */
    public function handleBeginRequest($event)
    {
        //@TODO check request and cache.
    }

    /**
     * Handle onEndRequest event.
     * Cache the request for re-request.
     */
    public function handleEndRequest($event)
    {
        //@TODO cache response
    }

    /**
     * Handle exception.
     * Set json response for exception.
     * @TODO Exception and error should be handled here or just handle to errorHandler component?
     */
    public function handleException($event)
    {
        $response = $this->getResponse();
        $response->setException($event->exception);
        $response->render();
        $event->handled = true;
    }

    /**
     * Handle error.
     * Set json response for error.
     * @TODO Exception and error should be handled here or just handle to errorHandler component?
     */
    public function handleError($event)
    {
        $response = $this->getResponse();
        $response->setError($event->code, $event->message);
        $response->render();
        $event->handled = true;
    }
}
