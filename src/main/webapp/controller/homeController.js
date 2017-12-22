var ToDo = angular.module("ToDo");
ToDo.controller('homeController',
				function($scope, noteService, labelService, toastr, $location,
						$uibModal, $interval, $filter ,$timeout,fileReader,$log) {
					
					$scope.notes = [];
					$scope.labels = [];
					$scope.labelName = [];
					$scope.colors = [];
					$scope.collaborators=[];
					$scope.users=[];
					$scope.owner;
					
					$scope.logoutUser = function() {
						logout();
					}
					
					/*------------------------Hiding the Note Data--------------------------------------------------*/
					var dontShow = function(){
						$('#note-title').hide();
						$('#note_icons').hide();
					}
					
					/*------------------------Check for Duplicate Label--------------------------------------------------*/
					var checkForDuplicateLabel = function(label){
						for(var labelNo=0;labelNo<$scope.labels.length;labelNo++){
							if($scope.labels[labelNo].labelName==label.labelName)
								return true;
							else
								return false;
						}
					}

					/*------------------------Navigation Bar Name Change--------------------------------------------------*/
					var navBarNameChange = function() {

						if ($location.path() == "/home") {
							$scope.navBarName = "FunDoNote";
							$scope.navBarColor = "#ff9900";
							setPageView($scope.navBarName);
						} else if ($location.path() == "/trash") {
							$scope.navBarName = "Trash";
							$scope.navBarColor = "#555457";
							setPageView($scope.navBarName);
						} else if ($location.path() == "/archive") {
							$scope.navBarName = "Archive";
							$scope.navBarColor = "#5B9B9D";
							setPageView($scope.navBarName);
						} else if ($location.path() == "/reminders") {
							$scope.navBarName = "Reminders";
							$scope.navBarColor = "#4E5C4D";
							setPageView($scope.navBarName);
						} else if ($location.path() == "/search") {
							$scope.navBarName = "FunDoNote";
							$scope.navBarColor = "#0734EF";
							setPageView($scope.navBarName);
						} else {
							$scope.navBarName = $location.path().substr(1);
							$scope.navBarColor = "#4E5C4D";
							setPageView($scope.navBarName);
						}
					}
					
					var setPageView = function(navBarName){
						ga('set', 'title', navBarName);
						ga('set', 'page', $location.path());
						ga('send', 'pageview');
					}
					
					$scope.setEvents = function(eventCategory,eventAction){
						ga('send','event',eventCategory,eventAction);
					}

					/*------------------------Constantly Check for reminder time--------------------------------------------------*/
					var intervalFunction = function() {
						$interval(
								function() {
									notes = $scope.notes;
									for (var noteCount = 0; noteCount < notes.length; noteCount++) {
										if (notes[noteCount].reminderDate != 0
												&& notes[noteCount].reminderTime != "") {
											var currentDate = $filter('date')(
													new Date(), 'yyyy-MM-dd');
											var remindedDate = $filter('date')(new Date(notes[noteCount].reminderDate),'yyyy-MM-dd');
											var currentTime = $filter('date')(new Date(), 'h:mm a');
											if (currentDate == remindedDate
													&& currentTime == notes[noteCount].reminderTime) {
												toastr.sucess(notes[noteCount].noteTitle,'Reminder');
												$scope.notes[noteCount].reminder = false;
												$scope.notes[noteCount].reminderDate = null;
												$scope.notes[noteCount].reminderTime = null;
												$scope.updateNote($scope.notes[noteCount]);
											}
										}
									}
								}, 30000);
					}
					
					/*------------------------Color Palette--------------------------------------------------*/
					var colors = [ {
						'color' : '#FFFFFF',
						'tooltip' : 'White',
						'path' : 'images/white.png'
					}, {
						'color' : '#F8BBD0',
						'tooltip' : 'Pink',
						'path' : 'images/pink.png'
					}, {
						'color' : '#DC94F7',
						'tooltip' : 'purple',
						'path' : 'images/purple.png'
					}, {
						'color' : '#82B1FF',
						'tooltip' : 'Dark blue',
						'path' : 'images/darkblue.png'
					}, {
						'color' : '#80D8FF',
						'tooltip' : 'Blue',
						'path' : 'images/blue.png'
					}, {
						'color' : '#CCFF90',
						'tooltip' : 'Green',
						'path' : 'images/green.png'
					}, {
						'color' : '#FF8A80',
						'tooltip' : 'Red',
						'path' : 'images/Red.png'
					}, {
						'color' : '#D5DBDB',
						'tooltip' : 'Grey',
						'path' : 'images/grey.png'
					}, {
						'color' : '#FFD180',
						'tooltip' : 'Orange',
						'path' : 'images/orange.png'
					}, {
						'color' : '#F5F518',
						'tooltip' : 'Yellow',
						'path' : 'images/lightyellow.png'
					}, {
						'color' : '#D7C9C8',
						'tooltip' : 'Brown',
						'path' : 'images/brown.png'
					} ];
					$scope.colors = colors;

					/*------------------------Initially Checks what view to present--------------------------------------------------*/
					var checkForView = function() {
						$scope.icon = localStorage.getItem('icon');
						if ($scope.icon == "glyphicon glyphicon-th-large black") {
							$scope.changeView = "col-md-12 col-sm-12 col-xs-12 col-lg-12";
							$scope.icon = "glyphicon glyphicon-th-large black";
						} else {
							$scope.changeView = "col-md-6 col-sm-10 col-xs-12 col-lg-4";
							$scope.icon = "glyphicon glyphicon-th-list black"
						}
					}

					/*------------------------Logout User--------------------------------------------------*/
					var logout = function() {
						localStorage.removeItem("token");
						localStorage.removeItem("icon");
						$location.path("login");
					}

					/*------------------------get Labels--------------------------------------------------*/
					var getLabels = function() {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'getLabels';
							var labels = noteService.service(url, 'GET', token);
							labels.then(function(response) {
								$scope.labels = response.data;
							});
						} else
							$location.path("login");
					}

					/*------------------------get Notes--------------------------------------------------*/
					var getNotes = function() {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'getNotes';
							var notes = noteService.service(url, 'GET', token);
							notes.then(function(response) {
								if (response.headers('Error') == "Expired")
									logout();
								else {
									getLabels();
									$scope.notes = response.data;
									$scope.changeToDateObject($scope.notes);
								}
							});
						} else
							$location.path("login");
					}
					
					/*------------------------Get The User Profile--------------------------------------------------*/
					var getTheOwner = function() {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'getOwner';
							var notes = noteService.service(url, 'GET', token);
							notes.then(function(response) {
								if (response.headers('Error') == "Expired")
									logout();
								else {
									getNotes();
									$scope.owner = response.data;
								}
							});
						} else
							$location.path("login");

					}
					
					/*------------------------Get All User Email--------------------------------------------------*/
					$scope.getAllUser = function(){
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'getAllUser';
							var labels = noteService.service(url, 'GET', token);
							labels.then(function(response) {
								$scope.users = response.data;
							});
						} else
							$location.path("login");
					} 
					
		/*********************************************************************************************************************************/

					/*------------------------Delete Note Permanently--------------------------------------------------*/
					$scope.deleteNotePermanently = function(note) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'deleteNote';
							var notes = noteService.service(url, 'PUT', token,
									note);
							notes.then(function(response) {
								if (response.data.message == "Token Expired")
									$location.path("login");
								else {
									getNotes();
									ga('send','event','Note Crud','Note Deleted Permanently');
								}	
							}, function(response) {
								getNotes();
								$scope.error = response.data.message;
							});
						} else
							$location.path("login");
					}

					/*------------------------Create Notes--------------------------------------------------*/
					$scope.createNote = function(note) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							if (note.noteTitle != ''
									|| note.noteDescription != '' || note.images != '') {
								var url = 'saveNote';
								var notes = noteService.service(url, 'POST',
										token, note);
								notes.then(function(response) {
													if (response.data.message == "Token Expired")
														$location.path("/login");
													else {
														getNotes();
														ga('send','event','NotesCrud','Create Note');
													}	
												},
												function(response) {
													$scope.error = response.data.message;

												});
							}
						} else
							$location.path("login");
					}

					/*------------------------Update Note --------------------------------------------------*/
					$scope.updateNote = function(note) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'updateNote';
							var notes = noteService.service(url, 'PUT', token,
									note);
							notes.then(function(response) {
								if (response.data.message == "Token Expired")
									$location.path("/login");
								else
									getNotes();
								$scope.getTheCollborators(note);
							}, function(response) {
								$scope.error = response.data.message;

							});
						} else
							$location.path("login");
					}
					
					/*------------------------Update User --------------------------------------------------*/
					$scope.updateUser = function(user) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'updateUser';
							var notes = noteService.service(url, 'PUT', token, user);
							notes.then(function(response) {
								if (response.data.message == "Token Expired")
									$location.path("/login");
								else {
									getTheOwner();
									ga('send','event','Image','Profile Picture Changed');
								}	
							}, function(response) {
								$scope.error = response.data.message;
							});
						} else
							$location.path("login");
					}

	/*********************************************************************************************************************************/				

					/*------------------------Changes view to on click--------------------------------------------------*/
					$scope.listView = function() {
						$scope.icon = localStorage.getItem('icon');
						if ($scope.icon == "glyphicon glyphicon-th-list black") {
							ga('send','event','View','View Changed To List');
							$scope.icon = "glyphicon glyphicon-th-large black";
							$scope.changeView = "col-md-12 col-sm-12 col-xs-12 col-lg-12";
							localStorage.setItem('icon', $scope.icon);
						} else {
							ga('send','event','View','View Changed To Grid');
							$scope.icon = "glyphicon glyphicon-th-list black"
							$scope.changeView = "col-md-6 col-sm-6 col-xs-12 col-lg-4";
							localStorage.setItem('icon', $scope.icon);
						}

					}
					
	
	/*********************************************************************************************************************************/	
					
					/*------------------------Facebook Sharing of Note--------------------------------------------------*/
					$scope.facebookShare = function(note) {
						FB.init({
							appId : '370919266686959',
							status : true,
							cookie : true,
							xfbml : true,
							version : 'v2.4'
						});
						
						FB.ui({
							  method: 'share_open_graph',
							  action_type: 'og.likes',
							  action_properties: JSON.stringify({
							    object:{
							    	'og:title' : note.noteTitle,
									'og:description' : note.noteDescription
							    }
							  })
							}, function(response){
							  console.log(response);
							  	if (response && !response.error_message) {
									toastr.success('Note shared', 'successfully');
									ga('send','event','Social Sharing','Note Shared To Facebook');
							  	} else {
									toastr.success('Note not shared', 'Error');
								}
							});
					}
					
	/*********************************************************************************************************************************/

					
					/*------------------------Getting Collaborators of the Note--------------------------------------------------*/
					$scope.getTheCollborators = function(note) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'getCollaborators';
							var collaborators = noteService.service(url, 'PUT', token , note);
							collaborators.then(function(response) {
								if (response.data.message == "Token Expired")
									$location.path("login");
								else
									note.collaborators = response.data;
							}, function(response) {
								$scope.error = response.data.message;
							});
						} else
							$location.path("login");
					}
					
					/*-------------------------Add Collaborators && Get The User Collaborated List---------------------------*/
					$scope.addCollaboraotrs = function(note,emailId) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							if(emailId!=""&&emailId!=undefined){
								var url = 'addCollaborator';
								var user = noteService.collaboratedUser(url, 'PUT', token, note, emailId);
								user.then(function(response) {
									if (response.headers('Error') == "Expired")
										logout();
									else {
										ga('send','event','Collaborators','Colaborator Added');
										$scope.collaborators = response.data;
										getNotes();
									}
								});
							}
						} else
							$location.path("login");
					}
					
					/*-------------------------Remove The User from Collaborated List---------------------------*/
					$scope.removeUserFromCollaboration = function($index,note){
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							ga('send','event','Collaborators','Colaborator Removed');
							note.collaboratedUser.splice($index,1);
							$scope.updateNote(note);
						}else
							$location.path("login");
					}
					
	/*********************************************************************************************************************************/

					/*-------------------------Delete all Notes From Trash---------------------------*/
					$scope.deleteAllNotesFromTrash = function() {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'emptyTrash';
							var notes = noteService.service(url, 'GET', token);
							notes.then(function(response) {
								if (response.data.message == "Token Expired")
									$location.path("login");
								else {
									getNotes();
									ga('send','event','NoteCrud','All Notes Deleted Together');
								}	
							}, function(response) {
								getNotes();
								$scope.error = response.data.message;
							});
						} else
							$location.path("login");
					}

					/*------------------------Delete Note--------------------------------------------------*/
					$scope.deleteNote = function(note) {
						note.trashed = true;
						note.archived = false;
						note.pinned = false;
						note.reminder = false;
						note.reminderDate = null;
						note.reminderTime = null;
						note.editedDate=new Date();
						ga('send','event','NotesCrud','Note Moved To Trash');
						$scope.updateNote(note);
					}
	
	/*********************************************************************************************************************************/


					/*------------------------Add reminder To Note--------------------------------------------------*/
					$scope.addReminderToNote = function(note, time) {
						if (note.reminderDate == 0 && note.reminderTime == "") {
							note.reminderDate = null;
							note.reminderTime = null;
						}
						ga('send','event','Reminders','Reminder Added To Note');
						note.reminderTime = time;
						note.reminder = true;
						$scope.updateNote(note);
					}

					/*------------------------remove reminder from Note--------------------------------------------------*/
					$scope.deleteReminderOfNote = function(note) {
						note.reminderDate = null;
						note.reminderTime = null;
						note.reminder = false;
						ga('send','event','Reminders','Reminder Removed From Note');
						$scope.updateNote(note);
					}
					
	/*********************************************************************************************************************************/
					
					/*------------------------Trigger the upload UI--------------------------------------------------*/
					$scope.triggerImageUploadUI = function(object,typeOfObject){
						$timeout(function(){
							$scope.type=object;
							$scope.typeOfObject=typeOfObject;
						$('#imageUploadUI').trigger('click');
						},0);
					}
					
					$scope.myImage='';
				    $scope.myCroppedImage='';
					
					/*------------------------upload photo to database--------------------------------------------------*/
					$scope.imageUpload = function(element){
					    var reader = new FileReader();
					    reader.onload = $scope.imageIsLoaded;
					    reader.readAsDataURL(element.files[0]);
					}
				
					$scope.imageIsLoaded = function(e){
					    $scope.$apply(function() {
					        var imageSrc=e.target.result;
					        if($scope.typeOfObject=='note'){
					        	$scope.type.image=imageSrc;
					        	ga('send','event','Image','Image Uploded To Notes');
					        	$scope.updateNote($scope.type);
					        }else if($scope.typeOfObject=='waitForCrop')
					        	$scope.myImage=imageSrc;
					    });
					};
					
	/*********************************************************************************************************************************/

					/*------------------------Get URL MetaDta--------------------------------------------------*/
					$scope.getURLMetaData = function(note) {
						var allmetaData=[];
						var alldata=note.noteDescription.split(/[ ;]+/);
						var urlArray=[];
						var pattern = /https?:\/\/[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?/gi;
						for(var urlNo=0;urlNo<alldata.length;urlNo++){
							if(pattern.test(alldata[urlNo])){
								var token = localStorage.getItem('token');
								if (token != null && token != "") {
									var url = 'urlMetadata';
									var urlData = noteService.getUrl(url, 'PUT', token , alldata[urlNo]);
									urlData.then(function(response) {
									if (response.data.message == "Token Expired")
										$location.path("login");
									else 
										allmetaData.push(response.data);
									});	
								} 
								else
									$location.path("login");
								note.link=allmetaData;
							}
						}
					}
					
	/*********************************************************************************************************************************/
					
					/*------------------------Remove Label--------------------------------------------------*/
					$scope.removeLabel = function(label) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							var url = 'deleteLabel';
							var labels = labelService.service(url, 'PUT',
									token, label);
							labels.then(function(response) {
								if (response.data.message == "Token Expired")
									$location.path("/login");
								else {
									getLabels();
									ga('send','event','Label','Label Removed');
								}	
							}, function(response) {
								$scope.error = response.data.message;

							});
						} else
							$location.path("login");
					}

					/*------------------------Update Label--------------------------------------------------*/
					$scope.updateLabel = function(label) {
						var token = localStorage.getItem('token');
						if (token != null && token != "") {
							console.log(label.labelName);
							var url = 'updateLabel';
							var labels = labelService.service(url, 'PUT',
									token, label);
							labels.then(function(response) {
								if (response.data.message == "Token Expired")
									$location.path("/login");
								else {
									getLabels();
									ga('send','event','Label','Label Updated');
								}	
							}, function(response) {
								$scope.error = response.data.message;

							});
						} else
							$location.path("login");
					}

					/*------------------------Add Label--------------------------------------------------*/
					$scope.addLabel = function(label) {
						if(!checkForDuplicateLabel(label)){
							var token = localStorage.getItem('token');
							if (token != null && token != "") {
								var url = 'createLabel';
								var labels = labelService.service(url, 'POST',
										token, label);
								labels.then(function(response) {
									if (response.data.message == "Token Expired")
										$location.path("/login");
									else {
										getLabels();
										ga('send','event','Label','Label Added');
									}	
								}, function(response) {
									$scope.error = response.data.message;

								});
							} else
								$location.path("login");
						}else
							document.getElementById("labelmessage").innerHTML = "Label Already Exists";
						
					}
					
					/*------------------------Check The Labels--------------------------------------------------*/
					$scope.checked = function(note, label) {
						var checkedLabels = note.labels;
						for (var labelNo = 0; labelNo < checkedLabels.length; labelNo++) {
							if (checkedLabels[labelNo].labelName == label.labelName)
								return true;
						}
						return false;
					}
					
					$scope.change = function(active, label, note, $index) {
						if (active) {
							$scope.labelName = label.labelName;
							$scope.attachLabelToNote(note, label);
						} else if (!active) {
							$scope.removeLabelFromNotes(note, label, $index);
						}
					};
					
					/*------------------------Update Note with Label--------------------------------------------------*/
					$scope.attachLabelToNote = function(note, label) {
						note.labeled = true;
						note.labels.push(label);
						ga('send','event','Label','Label Added To Note');
						$scope.updateNote(note);
					}

					/*------------------------Remove Label From Notes--------------------------------------------------*/
					$scope.removeLabelFromNotes = function(note, label, $index) {
						var url = 'updateNote';
						note.labels.splice($index, 1);
						if (note.labels.length == 0)
							note.labeled = false;
						ga('send','event','Label','Label Removed From Note');
						$scope.updateNote(note); 
					}
					
	/*********************************************************************************************************************************/	
					
					$scope.chageToSearch = function() {
						$location.path('search');
					}
					
					$scope.chageToHome = function() {
						$location.path('home');
					}
								
					$scope.changeToDateObject = function(notes) {
						for (var noteCount = 0; noteCount < notes.length; noteCount++) {
							notes[noteCount].reminderDate = new Date(notes[noteCount].reminderDate);
						}
					}

					$scope.showModal = function(note) {
						ga('send','event','UiModal','Note Editing Modal Opened');
						$scope.note = note;
						modalInstance = $uibModal.open({
							templateUrl : 'template/editNote.html',
							controller: 'ModalInstanceCtrl',
							scope : $scope,
							size : 'md',
						});
						
						modalInstance.result.then(
							function (result) {
							},
							function (result) {
								$log.info('Modal dismissed at: ' + new Date());
							}
						);
					};

					$scope.showModalLabel = function() {
						ga('send','event','UiModal','Label Modal Opened');
						$scope.modalInstance = $uibModal.open({
							templateUrl : 'template/LabelEdit.html',
							controller: 'ModalInstanceCtrl',
							scope : $scope,
							size : 'sm',
						});
						
						modalInstance.result.then(
							function (result) {
							},
							function (result) {
								$log.info('Modal dismissed at: ' + new Date());
							}
						);
					};
					
					$scope.showModalCollaborator = function(note) {
						ga('send','event','UiModal','Collaborator Modal Opened');
						$scope.note = note;
						var modalInstance = $uibModal.open({
							templateUrl : 'template/Collaborate.html',
							controller: 'ModalInstanceCtrl',
							scope : $scope,
							size : 'md',
						});
						
						modalInstance.result.then(
							function (result) {
							},
							function (result) {
								$log.info('Modal dismissed at: ' + new Date());
							}
						);
					};
					
					$scope.showImageUploader = function(user) {
						ga('send','event','UiModal','Image Uploader Modal Opened');
						$scope.user = user;
						modalInstance = $uibModal.open({
							templateUrl : 'template/imageuploadmodel.html',
							controller: 'ModalInstanceCtrl',
							scope : $scope,
							size : 'lg'
						});
						
						modalInstance.result.then(
							function (result) {
							},
							function (result) {
								$log.info('Modal dismissed at: ' + new Date());
							}
						);
					};
					
					$scope.showFullImageViewer = function(note) {
						ga('send','event','UiModal','Full Image Viewer Modal Opened');
						$scope.note = note;
						modalInstance = $uibModal.open({
							templateUrl : 'template/fullImage.html',
							controller: 'ModalInstanceCtrl',
							scope : $scope,
							size : 'md'
						});
						
						modalInstance.result.then(
							function (result) {
							},
							function (result) {
								$log.info('Modal dismissed at: ' + new Date());
							}
						);
					};
					
					$scope.matchingTheNotes = function(note,noteSearched) {
						var title = note.noteTitle.toLowerCase();
						var descrition = note.noteDescription.toLowerCase();
						if(noteSearched!=''&& noteSearched!=undefined){
							if(title.includes(noteSearched)|| descrition.includes(noteSearched))
								return true;
						}
						return false;
					}
					
					$scope.closeModal = function(){
						$scope.myImage='';
					    $scope.myCroppedImage='';
					}
					
					getTheOwner();

					navBarNameChange();

					intervalFunction();

					checkForView();
					
					dontShow();

				}).controller('ModalInstanceCtrl', ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
				    $scope.ok = function () {
				    	$uibModalInstance.close();
				    };
				}]);;