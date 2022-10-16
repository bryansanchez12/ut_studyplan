var program;
var student;
var student_id = 0;
var formsNumber = 0;
var mandatoryCourses;
var advancedCourses;
var submittedForms;
var selected_AdCourses = [];
var ethicsCourses;
var graduationCourses;
var electiveCourses;
var selected_ElCourses = [];
var totalECs = 0;
var restServlet = "http://localhost:8080/studyplan/rest/";


$( document ).ready(function() {
    getStudent();
    getNumberOfForms();
});

function getStudent() {
    var st = "";
    var name = ""
    var	http1 = new XMLHttpRequest();
    http1.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            st = JSON.parse(this.responseText);
            student_id = st.s_id;
            student = st;
            name = st.first_name + " " + st.surname;
            document.getElementById("st_name").innerText = "Hello, " + name;
        }
    };
    http1.open("GET", restServlet + "form/student", true);
    http1.send();
}

function logOut() {
    var	http1 = new XMLHttpRequest();
    http1.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            window.open(http1.responseURL, '_blank');
        }
    };
    http1.open("GET", restServlet + "form/logout/" + student_id, true);
    http1.send();
}

function getNumberOfForms(){
    var temp = 0;
    var txt = "", x;
    var	http = new XMLHttpRequest();
    http.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var submittedForms = JSON.parse(this.responseText);
            temp = 1;
            for (x in submittedForms) {
                temp+= 1;
            }
            formsNumber = temp;
            document.getElementById("no_forms").innerText = "You have "+temp+" form(s) created";
        }
    };
    http.open("GET", restServlet + "form/submitted_forms/"+ student_id, true);
    http.send();

}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        console.log(c)
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}


function calculateECs() {
    var danger = "<li class=\"list-group-item list-group-item-danger\" style=\"text-align:center\">";
    var warning = "<li class=\"list-group-item list-group-item-warning\" style=\"text-align:center\">";
    var goodEC = "<li class=\"list-group-item list-group-item-success\" style=\"text-align:center\">";
    if(totalECs < 120 && totalECs > 0){
        var temp = "<p class=\"alert\">You must complete until you have 120 credits</p>";
        document.getElementById("totalCredits").innerHTML = warning + totalECs.toString() + "</li>";
    }else if(totalECs >= 120){
        var temp = "<p class=\"success\">Yo have the sufficient credits. Great!</p>";
        document.getElementById("totalCredits").innerHTML = goodEC + totalECs.toString() + "</li>";
    } else {
        document.getElementById("totalCredits").innerHTML = danger + 0 + "</li>";
    }
}

function submitForm() {
    var form = document.getElementsByName("inputForm");

        var query = $('form').serialize();
        query += "&adCourses=" + selected_AdCourses;
        query += "&elCourses=" + selected_ElCourses;
        query += "&totalEC=" + totalECs;
        query += "&form_type=" + program;
        var http = new XMLHttpRequest()
        http.open("POST",  restServlet + "form/submitted")
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
        http.send(query);
        http.onload = function() {
            // Do whatever with response
            alert(http.responseText);
            if (this.readyState == 4 && this.status == 200) {
                alert("Form was submitted");
            } else {
                alert("Something went wrong")
            }
        }


}

var adCoursesCounter = 0;
var adCoursesECs = [];
function addAdCourses(id) {
    var checkbox = document.getElementById(id);
    if (checkbox.checked === true) {
        if(adCoursesCounter === 4){
            alert("You can only select 4 courses");
            checkbox.checked = false;
        }else {
            selected_AdCourses.push(id);
            for(var i = advancedCourses.length - 1; i >= 0; i--) {
                if(advancedCourses[i].course_code === id) {
                    totalECs += parseInt(advancedCourses[i].ec_number);
                    adCoursesECs.push(parseInt(advancedCourses[i].ec_number));
                }
            }
            adCoursesCounter += 1;
        }
    } else if(checkbox.checked === false) {
        for(var i = selected_AdCourses.length - 1; i >= 0; i--) {
            if(selected_AdCourses[i] === id) {
                selected_AdCourses.splice(i, 1);
                totalECs -= adCoursesECs[i];
                adCoursesECs.splice(i,1);
            }
        }
        adCoursesCounter -= 1;
    }
    calculateECs();
}

var elCoursesECs = [];
function addElCourses(id) {
    var checkbox = document.getElementById(id);
    if (checkbox.checked === true) {
        selected_ElCourses.push(id);
        for(var i = electiveCourses.length - 1; i >= 0; i--) {
            if(electiveCourses[i].course_code === id) {
                elCoursesECs.push(parseInt(electiveCourses[i].ec_number));
                totalECs += parseInt(electiveCourses[i].ec_number);
            }
        }
    } else if(checkbox.checked === false) {
        for(var i = selected_ElCourses.length - 1; i >= 0; i--) {
            if(selected_ElCourses[i] === id) {
                selected_ElCourses.splice(i, 1);
                totalECs -= elCoursesECs[i];
                elCoursesECs.splice(i,1);
            }
        }
    }
    calculateECs();
}

function changeInternship(checkbox) {
    if (checkbox.checked === true) {
        totalECs += 20;
    } else if(checkbox.checked === false) {
        totalECs -= 20;
    }
    calculateECs();
}

function editForm(form_id) {
    var temp;
    for (var x in submittedForms) {
        if(submittedForms[x].f_id === form_id ){
            temp = x;
        }
    }
    changeState(submittedForms[x].program);
    document.getElementById("inputName").value = student.first_name + " " + student.surname;
    document.getElementById("inputStudent").value = student_id;
    document.getElementById("inputEmail").value = student.email;
    document.getElementById("explanation").value = submittedForms[x].explanation;
    document.getElementById("explanation").value = submittedForms[x].explanation;
    document.getElementById("other_courses").value = submittedForms[x].otherCourses;
    document.getElementById("oCoursesExplanation").value = submittedForms[x].otherCoursesExplanation;
}

function getSubmittedForms() {
    var txt = "", x;
    var	http = new XMLHttpRequest();
    http.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            submittedForms = JSON.parse(this.responseText);
            txt += "<table id=\"submitted_forms_table\" class=\"table table-hover mt-4\">\n" +
                "       <thead>\n" +
                "            <tr>\n" +
                "               <th scope=\"col\">#</th>\n" +
                "               <th scope=\"col\">Form type</th>\n" +
                "               <th scope=\"col\">Date</th>\n" +
                "               <th scope=\"col\">Comment</th>\n" +
                "               <th scope=\"col\">State</th>\n" +
                "               <th scope=\"col\">Edit</th>\n" +
                "            </tr>\n" +
                "       </thead>\n" +
                "       <tbody>";
            var temp = 1;
            for (x in submittedForms) {
                txt += "<tr>\n" +
                    "      <th scope=\"row\">"+temp+"</th>\n" +
                    "      <td>"+ submittedForms[x].program + "</td>\n" +
                    "      <td>"+ submittedForms[x].submission_date + "</td>\n" +
                    "      <td>"+submittedForms[x].comment + "</td>\n";
                if(submittedForms[x].state == "Revision"){
                    txt += " <td><div class=\"alert alert-warning\">"+submittedForms[x].state+"</div></td>\n";
                    txt += "<td><div id\"buttonForm"+temp+"\"><button type=\"button\" class=\"btn btn-primary\" " +
                        "       id=\""+submittedForms[x].f_id+"\" " +
                        "       onclick=\"editForm("+submittedForms[x].f_id+")\">Edit</button></div></td>";
                } else if(submittedForms[x].state == "Approved"){
                    txt += " <td><div class=\"alert alert-success\">"+submittedForms[x].state+"</div></td>\n";
                } else {
                    txt += " <td><div class=\"alert alert-danger\">"+submittedForms[x].state+"</div></td>\n";
                }
                txt +=    "  </tr>";
                temp+= 1;
            }
            txt += "</tbody></table>";
            document.getElementById("div_table").innerHTML = txt;
        }
    };
    http.open("GET", restServlet + "form/submitted_forms/"+ student_id, true);
    http.send();
}

function getCourses(form) {
    program = form;
    <!-- GET Core Course -->
    var txtM = "", xM;
    var	xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            mandatoryCourses = JSON.parse(this.responseText);
            txtM += "<ul class=\"list-group list-group-flush\">";
            var head = "<li class=\"list-group-item\">";
            for (xM in mandatoryCourses) {
                txtM += head + mandatoryCourses[xM].course_code + " " +
                    mandatoryCourses[xM].course_name+"</li>";
                totalECs += parseInt(mandatoryCourses[xM].ec_number);
            }
            calculateECs();
            document.getElementById("mandatoryCourses").innerHTML = txtM;
        }
    };
    xmlhttp.open("GET", restServlet + "form/mandatory_courses/"+program, true);
    xmlhttp.send();

    <!-- GET ethics Course -->
    var txtET = "", xET;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            ethicsCourses = JSON.parse(this.responseText);
            txtET += "<ul class=\"list-group list-group-flush\">";
            var head = "<li class=\"list-group-item\">";
            for (var xET in ethicsCourses) {
                txtET += head + ethicsCourses[xET].course_code + " " +
                    ethicsCourses[xET].course_name+ "</li>";
                totalECs += parseInt(ethicsCourses[xET].ec_number);
            }
            calculateECs();
            document.getElementById("ethicsCourses").innerHTML = txtET;
        }
    };
    xmlhttp.open("GET", restServlet + "form/ethics_courses/"+program, true);
    xmlhttp.send();


    <!-- GET Graduation Course -->
    var txtG = "", xG;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            graduationCourses = JSON.parse(this.responseText);
            txtG += "<ul class=\"list-group list-group-flush\">";
            var head = "<li class=\"list-group-item\">";
            for (var xG in graduationCourses) {
                txtG += head + graduationCourses[xG].course_code + " "
                    +  graduationCourses[xG].course_name + "</li>";
                totalECs += parseInt(graduationCourses[xG].ec_number);
            }
            document.getElementById("graduationCourses").innerHTML = txtG;
        }
    };
    xmlhttp.open("GET", restServlet + "form/graduation_courses/" + program, true);
    xmlhttp.send();

    <!-- GET Advanced Course -->
    var txtA = "", xA;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            advancedCourses = JSON.parse(this.responseText);
            txtA += "<table id=\"advancedCourses_table\" class= \"table\"> " +
                "  <thead>\n" +
                "    <tr>\n" +
                "      <th scope=\"col\">Select Course</th>\n" +
                "      <th scope=\"col\">Course Name</th>\n" +
                "    </tr>\n" +
                "  </thead>\n" +
                "  <tbody>";
            for (xA in advancedCourses) {
                txtA += "<tr>\n" +
                    "        <td>\n" +
                    "            <div class=\"custom-control custom-checkbox\">\n" +
                    "                <input type=\"checkbox\" class=\"custom-control-input\" " +
                    "                       onclick= \"addAdCourses('"+ advancedCourses[xA].course_code + "')\"" +
                    "                       id=\""+ advancedCourses[xA].course_code + "\">\n" +
                    "                <label class=\"custom-control-label\" " +
                    "                       for=\""+ advancedCourses[xA].course_code + "\">"
                                                + advancedCourses[xA].course_code + "</label>\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td>" + advancedCourses[xA].course_name + "</td>\n" +
                    "   </tr>";
            }
            txtA += "</tbody></table>"
            document.getElementById("advancedCourses").innerHTML = txtA;
        }
    };
    xmlhttp.open("GET", restServlet + "form/advanced_courses/"+program, true);
    xmlhttp.send();

    <!-- GET Electives Course -->
    var txtEL = "", xEL;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            electiveCourses = JSON.parse(this.responseText);
            txtEL += "<table id=\"electiveCourses_table\" class= \"table\"> " +
                "  <thead>\n" +
                "    <tr>\n" +
                "      <th scope=\"col\">Select Course</th>\n" +
                "      <th scope=\"col\">Course Name</th>\n" +
                "    </tr>\n" +
                "  </thead>\n" +
                "  <tbody>";
            for (var xEL in electiveCourses) {
                txtEL += "<tr>\n" +
                    "        <td>\n" +
                    "            <div class=\"custom-control custom-checkbox\">\n" +
                    "                <input type=\"checkbox\" class=\"custom-control-input\" " +
                    "                       onclick= \"addElCourses('"+ electiveCourses[xEL].course_code + "')\"" +
                    "                       id=\""+ electiveCourses[xEL].course_code + "\">\n" +
                    "                <label class=\"custom-control-label\" " +
                    "                       for=\""+ electiveCourses[xEL].course_code + "\">"
                                            + electiveCourses[xA].course_code + "</label>\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td>" + electiveCourses[xEL].course_name + "</td>\n" +
                    "   </tr>";
            }
            calculateECs();
            document.getElementById("electivesCourses").innerHTML = txtEL;
        }
    };
    xmlhttp.open("GET", restServlet + "form/elective_courses/" + program, true);
    xmlhttp.send();
}

function changeState(text) {
    document.getElementById("student_page_content").innerHTML = form_html ;
    document.getElementById("div_title").style.paddingLeft = '510px';
    document.getElementById("div_title").style.paddingTop = '100px';
    document.getElementById("bg_image").style.height = '350px';
    document.getElementById("card").innerHTML = "";
    document.getElementById("app_name").innerHTML = "";
    totalECs = 0;
    if (text === 'DST'){
        document.title = 'DST Form';
        document.getElementById("bg_image").style.backgroundImage ='url(Pic_05.jpg)';
        document.getElementById("page_title").innerHTML
            = "<strong>DATA SCIENCE AND<br> TECHNOLOGY (DST)</strong>";
        document.getElementById("mentor_name").innerHTML
            = "<strong>Programme mentor:</strong> Dr.ir. Maurice van Keulen";
        getCourses("DST");
    } else if(text ==='IST'){
        document.title = 'IST Form';
        document.getElementById("bg_image").style.backgroundImage ='url(Pic_02.jpg)';
        document.getElementById("page_title").innerHTML
            = "<strong>INTERNET SCIENCE AND <br> TECHNOLOGY <br> (MSC/SPECIALIZATION)</strong>";
        document.getElementById("mentor_name").innerHTML
            = "<strong>Programme mentor:</strong> dr. Tom van Dijk and dr. Arnd Hartmanns";
        getCourses("IST");
    } else if(text === 'ST'){
        document.title = 'ST Form';
        document.getElementById("bg_image").style.backgroundImage ='url(Pic_09.jpg)';
        document.getElementById("page_title").innerHTML
            = "<strong>SOFTWARE TECHNOLOGY <br> (ST)</strong>";
        document.getElementById("mentor_name").innerHTML
            = "<strong>Programme mentor:</strong> Dr.ir. Pieter-Tjerk de Boer";
        getCourses("ST");
    } else if (text === 'Dashboard'){
        document.title = 'Dashboard';
        document.getElementById("bg_image").style.backgroundImage ='url(Pic_06.jpg)';
        document.getElementById("bg_image").style.height = '685px';
        document.getElementById("div_title").style.paddingTop = '50px';
        document.getElementById("div_title").style.paddingLeft = '600px';
        document.getElementById("page_title").innerHTML = "";
        document.getElementById("student_page_content").innerHTML = "";
        document.getElementById("card").innerHTML
            = "<div class=\" row wrapper card welcome_card col-8\">\n" +
            "       <div class=\"card-header\">\n" +
            "               <h1 ><strong>WELCOME!</strong></h1>\n" +
            "        </div>\n" +
            "        <div class=\"card-body\">\n" +
            "                <p id=\"st_name\">Hello, "+student.first_name + " " + student.surname+"</p>\n" +
            "                <p id=\"no_forms\">You have "+formsNumber+" form(s) created</p>" +
            "       </div>\n" +
            " </div>";
        document.getElementById("app_name").innerHTML
            = "<div class=\"row main\">\n" +
            "        <div class=\"app_name\">\n" +
            "                 <p>StudyPlan</p>\n" +
            "        </div>\n" +
            "  </div>";
    } else if(text ==='Submitted Forms'){
        document.title = 'Submitted Forms';
        document.getElementById("bg_image").style.backgroundImage ='url(Pic_02.jpg)';
        document.getElementById("page_title").innerHTML
            = "<strong>Submitted Forms</strong>";
        document.getElementById("student_page_content").innerHTML
            = "<div id=\"div_table\" class=\"mt-5\">No submitted forms found so far!</div>";
        getSubmittedForms();
    }
}

var form_html = "<div class=\"row mb-4 mt-5\">\n" +
    "                    <div id=\"formHeader\" class=\"col  offset-2\">\n" +
    "                        <h2 class=\"pl-5\">Academic year 2019-2020</h2>\n" +
    "                        <h6 class=\"pl-5\" id=\"mentor_name\">\n" +
    "                            <strong>Programme mentor:</strong> Dr.ir. Maurice van Keulen</h6>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "\n" +
    "                <div class=\"row\">\n" +
    "                    <form class=\"form needs-validation\" action=\"../studyplan/form\" method=\"POST\" name=\"inputform\">\n" +
    "\n" +
    "                        <div class=\"form-group row\">\n" +
    "                            <label for=\"inputName\" class=\"control-label col-4\">Name:</label>\n" +
    "                            <div class=\"col-7\">\n" +
    "                                <input type=\"name\" class=\"form-control\" id=\"inputName\" name=\"inputName\" required>\n" +
    "                                <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                <div class=\"invalid-feedback\">Please fill out this field.</div>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "\n" +
    "                        <div class=\"form-group row\">\n" +
    "                            <label for=\"inputStudent\" class=\"control-label col-4\">Student Number:</label>\n" +
    "                            <div class=\"col-7\">\n" +
    "                                <input type=\"text\" class=\"form-control\" id=\"inputStudent\" name=\"inputStudent\" required>\n" +
    "                                <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                <div class=\"invalid-feedback\">Please fill out this field.</div>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "\n" +
    "                        <!-- The grid of this object is misplaced -->\n" +
    "                        <div class=\"input-group form-group row mb-5\">\n" +
    "                            <label for=\"inputEmail\" class=\"control-label col-4\">Email Address:</label>\n" +
    "                            <div class=\" input-group-prepend col-8\">\n" +
    "                                <span class=\"input-group-text\">@</span>\n" +
    "                                <input type=\"name\" class=\"form-control\" id=\"inputEmail\" placeholder=\"@student.utwente.nl\"\n" +
    "                                       name=\"inputEmail\" required>\n" +
    "                                <div class=\"valid-feedback\">Valid.</div>\n" +
    "                                <div class=\"invalid-feedback\">Please fill out this field.</div>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "\n" +
    "                        <fieldset class=\"form-group row\">\n" +
    "\n" +
    "                            <div class=\"row\">\n" +
    "                                <div class=\"col-4\">\n" +
    "                                    <legend class=\"col-form-label pt-0\">\n" +
    "                                        Is this your first time filling out a course form for this specialization*\n" +
    "                                    </legend>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <div class=\"col-8\">\n" +
    "                                    <div class=\"form-check\">\n" +
    "                                        <input class=\"form-check-input\" type=\"radio\" name=\"boolean\" id=\"gridRadios1\" value=\"true\">\n" +
    "                                        <label class=\"form-check-label col-10\" for=\"gridRadios1\">\n" +
    "                                            Yes <br>\n" +
    "                                            Proceed by filling out the courses below.\n" +
    "                                        </label>\n" +
    "                                    </div>\n" +
    "                                    <div class=\"form-check\">\n" +
    "                                        <input class=\"form-check-input\" type=\"radio\" name=\"boolean\" id=\"gridRadios2\" value=\"false\">\n" +
    "                                        <label class=\"form-check-label col-10\" for=\"gridRadios2\">\n" +
    "                                            No <br>\n" +
    "                                            If you want to change a previously submitted form, please briefly explain\n" +
    "                                            your changes in the field below, before proceeding by filling out the course form.\n" +
    "                                        </label>\n" +
    "                                    </div>\n" +
    "                                </div>\n" +
    "                            </div>\n" +
    "\n" +
    "                            <div class=\"row col-8 mb-3 offset-2\">\n" +
    "                                <p><br>Brief explanation of the changes you're about to submit:</p>\n" +
    "                                <textarea class=\"form-control\" id=\"explanation\"" +
    "                                           name=\"explanation\" rows=\"3\"></textarea>\n" +
    "                            </div>\n" +
    "\n" +
    "                            <div class=\"offset-1\">\n" +
    "\n" +
    "\n" +
    "                                <h4>These courses are mandatory<br></h4>\n" +
    "\n" +
    "                                <div class=\"col-10 mb-3 bs-mandatory\">\n" +
    "                                    <div class=\"accordion\" id=\"accordionMandatory\">\n" +
    "\n" +
    "                                        <!-- Core courses card -->\n" +
    "                                        <div class=\"card\">\n" +
    "                                            <div class=\"card-header\" id=\"headingMandatory\">\n" +
    "                                                <h2 class=\"mb-0\">\n" +
    "                                                    <button type=\"button\" class=\"btn btn-link\" data-toggle=\"collapse\"\n" +
    "                                                            data-target=\"#collapseMandatory\">\n" +
    "                                                        <i class=\"fa fa-plus\"></i>    DST Core courses</button>\n" +
    "                                                </h2>\n" +
    "                                            </div>\n" +
    "\n" +
    "                                            <div id=\"collapseMandatory\" class=\"collapse\" aria-labelledby=\"headingMandatory\"\n" +
    "                                                 data-parent=\"#accordionMandatory\">\n" +
    "                                                <div class=\"card-body\">\n" +
    "                                                    <div class=\"card-body\">\n" +
    "                                                        <div class=\"container\" id=\"mandatoryCourses\">\n" +
    "                                                            <p>No core courses found so far!</p>\n" +
    "                                                        </div>\n" +
    "                                                    </div>\n" +
    "                                                </div>\n" +
    "                                            </div>\n" +
    "                                        </div>\n" +
    "\n" +
    "                                        <!-- Ethics courses card -->\n" +
    "                                        <div class=\"card\">\n" +
    "                                            <div class=\"card-header\" id=\"headingEthics\">\n" +
    "                                                <h2 class=\"mb-0\">\n" +
    "                                                    <button type=\"button\" class=\"btn btn-link\" data-toggle=\"collapse\"\n" +
    "                                                            data-target=\"#collapseEthics\">\n" +
    "                                                        <i class=\"fa fa-plus\"></i>    Ethics</button>\n" +
    "                                                </h2>\n" +
    "                                            </div>\n" +
    "\n" +
    "                                            <div id=\"collapseEthics\" class=\"collapse\" aria-labelledby=\"headingEthics\"\n" +
    "                                                 data-parent=\"#accordionMandatory\">\n" +
    "                                                <div class=\"card-body\">\n" +
    "                                                    <div class=\"card-body\">\n" +
    "                                                        <div class=\"container\" id=\"ethicsCourses\">\n" +
    "                                                            <p>No ethics courses found so far!</p>\n" +
    "                                                        </div>\n" +
    "                                                    </div>\n" +
    "                                                </div>\n" +
    "                                            </div>\n" +
    "                                        </div>\n" +
    "\n" +
    "                                        <!-- Graduation courses card -->\n" +
    "                                        <div class=\"card\">\n" +
    "                                            <div class=\"card-header\" id=\"headingGraduation\">\n" +
    "                                                <h2 class=\"mb-0\">\n" +
    "                                                       <button type=\"button\" class=\"btn btn-link\" data-toggle=\"collapse\"\n" +
    "                                                            data-target=\"#collapseGraduation\">\n" +
    "                                                        <i class=\"fa fa-plus\"></i>    Graduation</button>\n" +
    "                                                </h2>\n" +
    "                                            </div>\n" +
    "\n" +
    "                                            <div id=\"collapseGraduation\" class=\"collapse\" aria-labelledby=\"headingGraduation\"\n" +
    "                                                 data-parent=\"#accordionMandatory\">\n" +
    "                                                <div class=\"card-body\">\n" +
    "                                                    <div class=\"card-body\">\n" +
    "                                                        <div class=\"container\" id=\"graduationCourses\">\n" +
    "                                                            <p>No graduation courses found so far!</p>\n" +
    "                                                        </div>\n" +
    "                                                    </div>\n" +
    "                                                </div>\n" +
    "                                            </div>\n" +
    "                                        </div>\n" +
    "                                    </div>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <h4>These courses are electives<br></h4>\n" +
    "                                <h6><br>Please select 4 courses out of 6</h6>\n" +
    "\n" +
    "                                <div class=\"col-10 mb-3 bs-mandatory\">\n" +
    "                                    <div class=\"accordion\" id=\"accordionAdvanced\">\n" +
    "                                        <div class=\"card\">\n" +
    "                                            <div class=\"card-header\" id=\"headingAdvanced\">\n" +
    "                                                <h2 class=\"mb-0\">\n" +
    "                                                    <button type=\"button\" class=\"btn btn-link\" data-toggle=\"collapse\"\n" +
    "                                                            data-target=\"#collapseAdvanced\">\n" +
    "                                                        <i class=\"fa fa-plus\"></i> DST Advanced courses</button>\n" +
    "                                                </h2>\n" +
    "                                            </div>\n" +
    "                                            <div id=\"collapseAdvanced\" class=\"collapse\" aria-labelledby=\"headingAdvanced\"\n" +
    "                                                 data-parent=\"#accordionAdvanced\">\n" +
    "                                                <div class=\"card-body\">\n" +
    "                                                    <div class=\"container\" id=\"advancedCourses\">\n" +
    "                                                        <p>No advanced courses found so far!</p>\n" +
    "                                                    </div>\n" +
    "                                                </div>\n" +
    "                                            </div>\n" +
    "                                        </div>\n" +
    "                                    </div>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <div class=\"row col-10 mb-3\">\n" +
    "                                    <p>Profiling space: Data science students can specialize\n" +
    "                                        further in one or more of the following data science profiles.</p>\n" +
    "                                    <ul>\n" +
    "                                        <li><strong>a) specialist in specific kinds of data</strong>,\n" +
    "                                            such as natural language text, image data, geographic data,\n" +
    "                                            sensor data, networked data, </li>\n" +
    "                                        <li><strong>b) designer of smart services, </strong></li>\n" +
    "                                        <li><strong>c) designer of data science algorithms, </strong></li>\n" +
    "                                        <li><strong>d) multi-disciplinary researcher.</strong></li>\n" +
    "                                    </ul>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <h6>Please choose courses from this list</h6>\n" +
    "\n" +
    "                                <div class=\"col-10 mb-3 bs-mandatory\">\n" +
    "                                    <div class=\"accordion\" id=\"accordionElectives\">\n" +
    "                                        <div class=\"card\">\n" +
    "                                            <div class=\"card-header\" id=\"headingElectives\">\n" +
    "                                                <h2 class=\"mb-0\">\n" +
    "                                                    <button type=\"button\" class=\"btn btn-link\" data-toggle=\"collapse\"\n" +
    "                                                            data-target=\"#collapseElectives\">\n" +
    "                                                        <i class=\"fa fa-plus\"></i> Profiling space: suggested electives</button>\n" +
    "                                                </h2>\n" +
    "                                            </div>\n" +
    "                                            <div id=\"collapseElectives\" class=\"collapse\" aria-labelledby=\"headingElectives\"\n" +
    "                                                 data-parent=\"#accordionElectives\">\n" +
    "                                                <div class=\"card-body\">\n" +
    "                                                    <div class=\"container\" id=\"electivesCourses\">\n" +
    "                                                        <p>No electives courses found so far!</p>\n" +
    "                                                    </div>\n" +
    "                                                </div>\n" +
    "                                            </div>\n" +
    "                                        </div>\n" +
    "                                    </div>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <p>Apart from the above choices, students can also pick:</p>\n" +
    "\n" +
    "                                <div class=\"row col-10\">\n" +
    "                                    <div class=\"col-4\">\n" +
    "                                        <p>Internship (Optional)</p>\n" +
    "                                    </div>\n" +
    "                                    <div class=\"checkbox col-4\">\n" +
    "                                        <label>\n" +
    "                                            <input type=\"checkbox\" id=\"internship\" " +
    "                                              name=\"internship\" onclick=\"changeInternship(this)\" value=\"true\">\n" +
    "                                            Internship 20EC\n" +
    "                                        </label>\n" +
    "                                    </div>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <div class=\"row col-10 mb-3\">\n" +
    "                                    <ul>\n" +
    "                                        <li>(a,c) other courses on fundamentals and algorithms of\n" +
    "                                            signal processing, stochastic processing,\n" +
    "                                            spatio/temporal data processing, etc.</li>\n" +
    "                                        <li>(d) other courses on data analysis from fields like\n" +
    "                                            health/medicine, social sciences, business sciences,\n" +
    "                                            bio-informatics, engineering and geo-informatics. </li>\n" +
    "                                    </ul>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <div class=\"row col-10 mb-3\">\n" +
    "                                    <p>Profiling space: Other courses (course code, course name):</p>\n" +
    "                                    <textarea class=\"form-control\" rows=\"3\" id=\"other_courses\" "+
    "                                           name=\"other_courses\"></textarea>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <div class=\"row col-10 mb-3\">\n" +
    "                                    <p>If you proposed \"other courses\" above, please explain\n" +
    "                                        how these form a coherent DS&T programme targeting\n" +
    "                                        your specific individual goals\n" +
    "                                    </p>\n" +
    "                                    <textarea class=\"form-control\" rows=\"3\" id=\"oCoursesExplanation\" " +
    "                                           name=\"oCoursesExplanation\"></textarea>\n" +
    "                                </div>\n" +
    "\n" +
    "                                <p>* indicates a required field</p>\n" +
    "\n" +
    "                                <div class=\"row col-10 mb-3\">\n" +
    "                                    <div class=\"col-3\">\n" +
    "                                        <p>Total credits (EC):*</p>\n" +
    "                                    </div>\n" +
    "                                    <div class=\"col-4\">\n" +
    "                                        <ul class=\"list-group\" id=\"totalCredits\">\n" +
    "                                            <li class=\"list-group-item list-group-item-danger\"\n" +
    "                                                style=\"text-align:center\">0</li>\n" +
    "                                        </ul>\n" +
    "                                    </div>\n" +
    "                                    <div class=\"col-4\">\n" +
    "                                        <button type=\"button\" class=\"btn btn-primary\" id=\"submit\"\n" +
    "                                                onclick=\"submitForm()\">Submit</button>\n" +
    "                                    </div>\n" +
    "                                </div>\n" +
    "                            </div>\n" +
    "                        </fieldset>\n" +
    "                    </form>\n" +
    "                </div>"