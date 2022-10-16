var restServlet = "http://localhost:8080/studyplan/rest/";
var form = document.getElementById("form");

function validateLogin() {
    if (form.checkValidity() === true){
        alert("i am working")
        var query = $('form').serialize();
        var http = new XMLHttpRequest();
        http.open("POST", restServlet + "login/validate", true);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send(query);
        http.onload = function() {
            alert("Wrong credentials, please login again")
            // Do whatever with response
            //document.open();
            //document.write(http.responseText);
            //document.close();
        }

    } else{
        event.preventDefault();
        event.stopPropagation();
    }
}

function createUser() {
    document.getElementById("card_content").innerHTML = sign_up ;
    document.getElementById("card").style.height = '600px';
    document.getElementById("card").style.marginTop = '10px';
    document.getElementById("login_page_content").style.paddingTop = '20px';
    document.getElementById("card_footer").innerHTML = "";

}

var sign_up = "<form action=\"../studyplan/rest/login/create_user\" method=\"POST\" class=\"needs-validation\" name=\"form\" id=\"form\" novalidate>\n" +
    "                            <div class=\"form-group\">\n" +
    "                                <label for=\"inputStNumber\">Student Number: </label>\n" +
    "                                <input type=\"text\" class=\"form-control\" id=\"inputStNumber\" placeholder=\"1406875\"\n" +
    "                                       name=\"inputStNumber\" required>\n" +
    "                                <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                <div class=\"invalid-feedback\"><p>Please fill out with a correct Student Number.</p></div>\n" +
    "                            </div>\n" +
    "\n" +
    "                            <div class=\"form-group\">\n" +
    "                                <label for=\"inputFsName\">First Name: </label>\n" +
    "                                <input type=\"text\" class=\"form-control\" id=\"inputFsName\" name=\"inputFsName\" required>\n" +
    "                                <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                <div class=\"invalid-feedback\"><p>Please fill out with a correct First Name.</p></div>\n" +
    "                            </div>\n" +
    "\n" +
    "                            <div class=\"form-group\">\n" +
    "                                <label for=\"inputSurname\">Surname: </label>\n" +
    "                                <input type=\"text\" class=\"form-control\" id=\"inputSurname\" name=\"inputSurname\" required>\n" +
    "                                <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                <div class=\"invalid-feedback\"><p>Please fill out with a correct Surname.</p></div>\n" +
    "                            </div>\n" +
    "\n" +
    "                              <div class=\"form-group\">\n" +
    "                                  <label for=\"inputEmail\">Email:</label>\n" +
    "                                  <input type=\"text\" class=\"form-control\" id=\"inputEmail\" placeholder=\"@student.utwente.nl\"\n" +
    "                                         name=\"inputEmail\" required>\n" +
    "                                  <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                  <div class=\"invalid-feedback\"><p>Please fill out with a correct email address.</p></div>\n" +
    "                              </div>\n" +
    "\n" +
    "                              <div class=\"form-group\">\n" +
    "                                  <label for=\"inputPassword\">Password:</label>\n" +
    "                                  <input type=\"password\" class=\"form-control\" id=\"inputPassword\" placeholder=\"Enter password\"\n" +
    "                                         name=\"inputPassword\" required>\n" +
    "                                  <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                  <div class=\"invalid-feedback\"><p>Please fill out with a correct password.</p></div>\n" +
    "                              </div>\n" +
    "\n" +
    "                              <button type=\"submit\" class=\"btn float-right login_btn text-light\">Sign up</button>\n" +
    "                          </form>";