<?php

/******************************************************************
 * CSV Import for FreshBooks API
 * ----------------------------------------------------------------
 * Version: 1.1
 * Date: January 18th, 2008
 * 
 * Copyright (c) 2007 FreshBooks (a service of 2ndSite Inc.)
 * 
 * This work is distributed under the MIT License:
 * 	 http://www.opensource.org/licenses/mit-license.php
 * 
******************************************************************/

/**
 * Send the request via HTTP
 * 
 * @param $url FreshBooks API endpoint
 * @param $key Authentication token
 * @param $xml Request body
 */
function sendRequest($url, $key, $xml){
	$ch = curl_init();                           // initialize curl handle
	curl_setopt($ch, CURLOPT_URL, $url);         // set url to post to
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); // return into a variable
	curl_setopt($ch, CURLOPT_TIMEOUT, 4);        // times out after 4s
	curl_setopt($ch, CURLOPT_POSTFIELDS, $xml);  // add POST fields
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE); // turn off SSL verification
	curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE); // turn off SSL verification
	curl_setopt($ch, CURLOPT_USERPWD, $key);
	
	$result = curl_exec($ch); // run the whole process
	curl_close ($ch);
	
	if (strlen($result) < 2) 
		$result = "Could not execute curl.";
		
	preg_match_all ("/<(.*?)>(.*?)\</", $result, $outarr, PREG_SET_ORDER);
	$n = 0;
	while (isset($outarr[$n])){
		$retarr[$outarr[$n][1]] = strip_tags($outarr[$n][0]);
		$n++;
	}
	return $result;
}

//----------------------------------------
// Begin Script
//----------------------------------------
if (isset($_POST['submit'])) {
	$url = $_POST['xmlurl'];
	$key = $_POST['key'];
	
	$csv = stripslashes($_POST['csv']);
	$lines = explode("\n", $csv);
	
	$count = 1; 
	foreach ($lines as $line){
		if (trim($line) == '')
			continue;
			
		$itemElement = explode(",", trim($line));
					
		$xmlItem =<<<EOL
<?xml version="1.0" encoding="UTF-8"?>
<request method="item.create">
	<item>
		<name>$itemElement[0]</name>
		<description>$itemElement[1]</description>
		<unit_cost>$itemElement[2]</unit_cost>
		<quantity>$itemElement[3]</quantity>
	</item>
</request>
EOL;
		$result .= "Result $count: ". sendRequest($url,$key,$xmlItem) . "\n";
		$count++;
	}
}

if (!$url) { 
	$url = "https://yourcompanyname.freshbooks.com/api/xml-in";
}
	
$defaultText = "ItemName1,Description1,UnitCost1,Quantity1\nItemName2,Description2,UnitCost2,Quantity2";
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Import CSV to Items</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	</head>
	<body>
	<style type="text/css">
		h2{font-family: Arial,Helvetica,sans-serif;font-size: 16px;color: #000000;}
	</style>
	<FORM NAME="form" METHOD=POST ACTION="<?php echo($PHP_SELF); ?>">
		<input type=hidden name='submit'>
		<h2>XML URL:&nbsp;<input size="50" name="xmlurl" value="<? echo stripslashes($url); ?>" ></h2>
		<h2>Authentication Token:&nbsp;<input size="35" name="key" value="<?= stripslashes($key); ?>"></h2>
		<h2>CSV Input:</h2>
		<textarea cols="70" rows="15" name="csv" ><? if ($csv == "") echo $defaultText;	else echo stripslashes($csv);?></textarea>
		<br/><br/>
		<input name='sendxml' type='submit' value='Submit' alt='Submit' align='bottom'><br/>
		<h2>Response:</h2>
		<pre style="background-color:#eee;padding:10px 12px"><code><?
			if ($result) {
				$viewxmlresp = 1;
				echo htmlspecialchars($result);
			}?>
		</code></pre>
	</form>
</body>
</html>