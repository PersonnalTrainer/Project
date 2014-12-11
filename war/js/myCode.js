var isCo = false;

//Fonction pour l'enregistrement des utilisateurs (form.html)
$(document).ready(function(){
	$("#btnCommitId").click(function(){
		$.post("register",
				{
			cmd:"Register",
			email:$("#inputEmailId").val(),
			pwd:$("#inputPwdId").val(),
			login:$("#inputLoginId").val(),
				},
				function(data,status){
					alert("Post Done new user added, id: " + data.userid + "\nStatus: " + status);
				});
	});

	// Pour la page personnal Data
	$.post("findTraining",
			{},
			function(data,status){

				var table = document.getElementById("trainingtable");
				//var mysize = data.size;
				var mysize = data.length;
				

				for (var i = 0; i < mysize; i++) {
					var numberRows = table.rows.length;
					var row = table.insertRow(numberRows);

					var name = row.insertCell(0);
					var time = row.insertCell(1);
					var completed = row.insertCell(2);

//					var var2 = data.name1;
//					var var1 = "data.name" + i;
					var myTrain = data[i];
					name.innerHTML = '<label class="btn">'+myTrain.name+'</label>';
					time.innerHTML = '<label class="btn"> <span class="glyphicon glyphicon-time"></span>'+myTrain.time+'min </label>';
					completed.innerHTML = '<label class="btn">'+myTrain.completed+'</label>';
					//completed.innerHTML = '<button type="submit" class="btn btn-link">'+ data.completed1 +'</button>';
				}


				//$("#trainingtable").html("<td>");
				//if(data.usermail != "Veuillez vous identifier") isCo = true;
			});

	// Test si le mec est co 
	$.post("checkUser",
			{},
			function(data,status){
				$("#textreturn").html(data.usermail);
				if(data.usermail != "Veuillez vous identifier") isCo = true;
			});

	// Au moment du click s'il est co on le redirige
	$("#buttonUser").click(function(){
		if(isCo == true)
		{
			document.location.href="personnalData.html"
		}
	});


	//Fonction pour le message d'acceuil du splash screen (index.html)
	$.post("accueilmessage",
			{ },
			function(data,status){
				$("#textcache").html(data.messagecache);
			});

	//Fonction pour l'identification des utilisateurs
	$.get("mylogin",
			{},
			function(data,status){
				$("#textLogged").html(data.Machaine);
			});

	$("#addExercice").click(function(){
		if($("#titleDescription").val()=="" || $("#exerciceDescription").val() =="" || $("#dureeExerciceH").val() =="" || $("#dureeExerciceM").val() == "")
			return;

		var table = document.getElementById("listExerciceOfTrainingPlan");

		var numberRows = table.rows.length;
		var position=0;

		if (numberRows != 0)
			position = parseInt(table.rows[numberRows-1].cells[0].innerHTML);


		var row = table.insertRow(numberRows);

		var positionTable = row.insertCell(0);
		var titleTable = row.insertCell(1);
		var descriptionTable = row.insertCell(2);
		descriptionTable.className = "hidden-xs";
		var dureeTable = row.insertCell(3);
		var suprimerTable = row.insertCell(4);

		// Add some text to the new cells:
		positionTable.innerHTML = (position +1).toString();
		titleTable.innerHTML =  $("#titleDescription").val();
		descriptionTable.innerHTML = "<p>"+$("#exerciceDescription").val()+"</p>";

		var duree = parseInt($("#dureeExerciceH").val())*60+parseInt($("#dureeExerciceM").val());  		

		dureeTable.innerHTML = duree + "min";

		suprimerTable.innerHTML = '<button onclick="supprimerExercice('+position.toString()+');" type="button" class="btn btn-danger btn-sm"> <span class="glyphicon glyphicon-remove"></span> </button>';

		document.getElementById("titleDescription").value = "";
		document.getElementById("exerciceDescription").value = "";
		document.getElementById("dureeExerciceH").value = "";
		document.getElementById("dureeExerciceM").value = "";
		//document.getElementById("dureeExerciceS").value = "";

		actualiserTotalTimeTraining();
	});


	$("#addTrainingPlan").click(function(){
		var table = document.getElementById("listExerciceOfTrainingPlan");
		var tabletoString="";

		for (i = 0; i < table.rows.length; i++) { 
			for(j=0;j<table.rows[i].cells.length-1;j++){
				tabletoString += table.rows[i].cells[j].innerHTML + "nextValue";
				if(j== table.rows[i].cells.length-2)
					tabletoString += "nextLine";  
			}
		}


		$.post("addtrainingplan",
				{
			title:$("#inputTitle").val(),
			description:$("#inputDescription").val(),
			domain:$("#e1").val(),
			table:tabletoString,
				},
				function(data,status){
					alert("le TrainingPlan "+$("#inputTitle").val() +" a bien été ajouté");
				});
	});


});

//a rajouter!
function actualiserTotalTimeTraining() {
	// the function returns the product of p1 and p2
	var table = document.getElementById("listExerciceOfTrainingPlan");

	var numberRows = table.rows.length;
	var timeMin = 0;

	for (i = 0; i < table.rows.length; i++) { 
		var time = table.rows[i].cells[3].innerHTML;
		time = time.replace("min","");
		timeMin += parseInt(time);
	}
	$("#totalTimeValue").html('<span class="glyphicon glyphicon-time"></span>'+timeMin.toString()+'min</p>');

}

function supprimerExercice(positionToDelete){
	var table = document.getElementById("listExerciceOfTrainingPlan");
	table.deleteRow(positionToDelete);

	for (i = 0; i < table.rows.length; i++) { 
		table.rows[i].cells[0].innerHTML = i+1;
	}


	actualiserTotalTimeTraining(); 
}

function redirectResultPage(valueButton){
	document.location.href="ha-result-screen.html?domain="+valueButton; 


}

function redirectResultPageKeyword(){
	var value = $("#inputKeywords").val();
	value.split(' ').join('_');
	document.location.href="ha-result-screen.html?keywords="+value; 


}

