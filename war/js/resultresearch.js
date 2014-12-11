$(document).ready(function(){
	var getsearchvalue =getQuerystring('domain');

	if(getsearchvalue != "")
		searchByDomain(getsearchvalue);
	else
	{
		getsearchvalue = getQuerystring('keywords');
		searchByKeywords(getsearchvalue);
	}

  
});

function getQuerystring(key, default_)
{
  if (default_==null) default_=""; 
  key = key.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regex = new RegExp("[\\?&]"+key+"=([^&#]*)");
  var qs = regex.exec(window.location.href);
  if(qs == null)
    return default_;
  else
    return qs[1];
}

function searchByDomain(getsearchvalue)
{
	$.post("resultresearch",
	  { 
  		domain:getsearchvalue,
	  },
	  function(data,status){
		  ajouterTraingEtExerciceDansTable(data.training,data.exercice);
		  });
  
}

function searchByKeywords(getsearchvalue){
	$.post("resultresearchkeywords",
			  { 
		  		keywords:getsearchvalue,
			  },
			  function(data,status){
				  
				  ajouterTraingEtExerciceDansTable(data.training,data.exercice);
				  
			  });
}

function redirectResultPageKeyword(){
	var value = $("#inputKeywords").val();
	value = value.split(' ').join('_');
	document.location.href="ha-result-screen.html?keywords="+value; 	
}

function ajouterTraingEtExerciceDansTable(paramTraining,paramExercice)
{
	  var table = document.getElementById("listTrainingPlan");
	  var tableEx = document.getElementById("listExercice");
	  
	  var trainingstring = paramTraining;
	  var exercicestring = paramExercice;
	  var trainingArray = trainingstring.split("nexttraining");
	  var exerciceArray = exercicestring.split("nexttraining");
	  
	  for (var i = 0; i < trainingArray.length-1; i++) {
		  var numberRows = table.rows.length;
	  	  var row = table.insertRow(numberRows);
	  	  var titleTraining = row.insertCell(0);
		  var dureeTraining = row.insertCell(1);
		
	      var InfoTraining = trainingArray[i].split("nextvalue");
	      
	      titleTraining.innerHTML = '<button type="submit" class="btn btn-link">'+ InfoTraining[1] +'</button>';
	      dureeTraining.innerHTML = '<label class="btn"> <span class="glyphicon glyphicon-time"></span>'+InfoTraining[2]+'</label>';
	  }
	  
	  for (var i = 0; i < exerciceArray.length-1; i++) {
		  var numberRows = tableEx.rows.length;
	  	  var row = tableEx.insertRow(numberRows);
	  	  var titleExercice = row.insertCell(0);
		  var dureeExercice = row.insertCell(1);
		
	      var InfoExercice = exerciceArray[i].split("nextvalue");
	      
	      titleExercice.innerHTML = '<button type="submit" class="btn btn-link">'+ InfoExercice[1] +'</button>';
	      dureeExercice.innerHTML = '<label class="btn"> <span class="glyphicon glyphicon-time"></span>'+InfoExercice[2]+'</label>';
		  }
}