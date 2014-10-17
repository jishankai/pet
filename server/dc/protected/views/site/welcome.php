<h1>上传欢迎页面</h1>
<form action="<? echo Yii::app()->createUrl('site/welcome')?>" method="post"
enctype="multipart/form-data">
<label for="image">Image:</label>
<input type="file" name="image" id="image" /> 
<br />
<input type="submit" name="submit" value="Submit" />
</form>
