(function(window, document, undefined) {

    // pane elements
    var rightPane = document.getElementById('right-pane');
    // TODO: add other panes here

    // button and input elements
    // TODO: add button/input element selectors here

    // script elements that correspond to Handlebars templates
    var questionFormTemplate = document.getElementById('question-form-template');
    var questionTemplate = document.getElementById('questions-template');
    var expandedTemplate = document.getElementById('expanded-question-template');

    // compiled Handlebars templates
    var templates = {
        renderQuestionForm: Handlebars.compile(questionFormTemplate.innerHTML),
        renderQuestion: Handlebars.compile(questionTemplate.innerHTML),
        renderExpandedQuestion: Handlebars.compile(expandedTemplate.innerHTML)
    };

    /* Returns the questions stored in localStorage. */
    function getStoredQuestions() {
        if (!localStorage.questions) {
            // default to empty array
            localStorage.questions = JSON.stringify([]);
        }

        return JSON.parse(localStorage.questions);
    }

    /* Store the given questions array in localStorage.
     *
     * Arguments:
     * questions -- the questions array to store in localStorage
     */
    function storeQuestions(questions) {
        localStorage.questions = JSON.stringify(questions);
    }

    // Adds event listeners to all questions on the side bar, so if clicked, right pane shows up
    function addEventListenersToAll(){
        var storedQuestionElements = document.getElementsByClassName("question-info");
        for (i = 0; i < storedQuestionElements.length; i++) {
            (function(){
                var k = i;
                var eachQuestion = storedQuestionElements[k];
                eachQuestion.addEventListener("click", function(event){
                    var storedRightQuestionPanel = templates.renderExpandedQuestion(searchObject.questions[k]);
                    document.getElementById('right-pane').innerHTML = storedRightQuestionPanel;
                    addEventListenerToRightPanelSubmit(k);
                    addEventListenerToRightPanelResolve(searchObject.questions[k]);
                    event.preventDefault();
                });
            }())
        }
    }

    // Task 4: add response functionality, saves to localstorage
    function addEventListenerToRightPanelSubmit(index){
        var responseForm = document.getElementById('response-form');
        responseForm.addEventListener("submit", function(event){
            var name_val = document.getElementById('name_box').value;
            var response_val = document.getElementById('response_box').value;
            questionsObject.questions[index].responses.push({
                name: name_val,
                response: response_val
            });
            var rightQuestionPanel = templates.renderExpandedQuestion(questionsObject.questions[index]);
            document.getElementById('right-pane').innerHTML = rightQuestionPanel;
            storeQuestions(questionsObject.questions);
            addEventListenerToRightPanelSubmit(index);
            event.preventDefault();
        });
    }

    // Adds functionality to the resolve button, takes into account whether there is a search string or not
    function addEventListenerToRightPanelResolve(obj){
        var resolveButton = document.getElementsByClassName('resolve')[0];
        resolveButton.addEventListener("click", function(event){
            var index = 0;
            for (var i = 0; i < questionsObject.questions.length; i++) {
                (function(){
                    var k = i;
                    if(obj.subject == questionsObject.questions[k].subject && obj.question == questionsObject.questions[k].question){
                        index = k;
                    }
                }())
            }
            questionsObject.questions.splice(index, 1);
            storeQuestions(questionsObject.questions);
            filterQuestions(document.getElementById('search').value);
            document.getElementById('left-pane').innerHTML = templates.renderQuestion(searchObject);
            document.getElementById("right-pane").innerHTML = templates.renderQuestionForm();
            addNewQuestions();
            addEventListenersToAll();
            event.preventDefault();
        });
    }
    // TODO: tasks 1-5 and one extension

    // display question form initially
    rightPane.innerHTML = templates.renderQuestionForm();

    // object that stores all the questions
    var questionsObject = {
        questions:[]
    }

    // object that stores all the questions currently being filtered with
    var searchObject = {
        questions:[]
    }

    // Task 1: Add new Questions
    function addNewQuestions(){
        var submitButton = document.getElementById('question-form');
        submitButton.addEventListener("submit", function(event){
            var subject_val = document.getElementById('subject_box').value;
            var question_val = document.getElementById('question_box').value;
            questionsObject.questions.push({
                subject: subject_val,
                question: question_val,
                responses:[]
            });
            var questionPanel = templates.renderQuestion(questionsObject);
            document.getElementById('left-pane').innerHTML = questionPanel;
            // Clearing the form fields
            document.getElementById('subject_box').value = "";
            document.getElementById('question_box').value = "";
            storeQuestions(questionsObject.questions);

            filterQuestions(document.getElementById('search').value);
            // Task 2: Add event listener to .question-info
            addEventListenersToAll();
            event.preventDefault();
        });
    }

    // Extension: filters the questions on the side by subject and question, adds to searchObject
    function filterQuestions(text){
        searchObject.questions = [];
        for (var i = 0; i < questionsObject.questions.length; i++) {
            var currentQuestionQuery = questionsObject.questions[i];
            if(currentQuestionQuery.subject.indexOf(text) != -1 || currentQuestionQuery.question.indexOf(text) != -1){
                searchObject.questions.push(currentQuestionQuery);
            }
        }
        var filterPanel = templates.renderQuestion(searchObject);
        document.getElementById('left-pane').innerHTML = filterPanel;
        addEventListenersToAll();
    }

    // Task 1: refreshing page stays same
    questionsObject.questions = getStoredQuestions();
    searchObject.questions = getStoredQuestions();
    var questionPanel = templates.renderQuestion(questionsObject);
    document.getElementById('left-pane').innerHTML = questionPanel;
    addNewQuestions();
    // Task 2: adding event listeners
    addEventListenersToAll();
    // Task 3: new question
    var newButton = document.getElementById("interactors").getElementsByClassName("btn")[0];
    newButton.addEventListener("click", function(event){
        document.getElementById("right-pane").innerHTML = templates.renderQuestionForm();
        addNewQuestions();
        event.preventDefault();
    });
    // Extension: Search
    var searchText = document.getElementById('search');
    searchText.addEventListener("keyup", function(event){
        filterQuestions(searchText.value);
    });

})(this, this.document);
