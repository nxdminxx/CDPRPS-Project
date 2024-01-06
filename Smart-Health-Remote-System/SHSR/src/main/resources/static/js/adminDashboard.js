const userTable = document.getElementsByClassName('user_table');
buttonTable = document.querySelectorAll('.admin_navBtn');

function toggleSkills() {
    let itemClass = this.className;

    for (i = 0; i < userTable.length; i++) {
        userTable[i].className = 'user_table';
        buttonTable[i].classList.remove('admin_navBtn_active');
    }
    if (itemClass === 'patientBtn admin_navBtn') {
        userTable[0].className = "user_table tableActive";
        this.classList.add("admin_navBtn_active");
    } else if (itemClass === 'doctorBtn admin_navBtn') {
        userTable[1].className = "user_table tableActive";
        this.classList.add("admin_navBtn_active");
    } else if (itemClass === 'adminBtn admin_navBtn') {
        userTable[2].className = "user_table tableActive";
        this.classList.add("admin_navBtn_active");
    }else if (itemClass === 'asspatBtn admin_navBtn') {
        userTable[3].className = "user_table tableActive";
        this.classList.add("admin_navBtn_active");
    }
    
}

buttonTable.forEach((el) => {
    el.addEventListener('click', toggleSkills);
});

const userForm = document.getElementsByClassName('extraForm'),
    radioFormInput = document.querySelectorAll('input[type=radio][name="role"]');

function toggleForm() {
    let radioClass = this.className;

    for (i = 0; i < userForm.length; i++) {
        userForm[i].className = 'extraForm form-group';
    }

    if (radioClass === 'form-check-input patient') {
        userForm[0].className = "extraForm form-group activeForm";
    } else if (radioClass === 'form-check-input doctor') {
        userForm[1].className = "extraForm form-group activeForm";
    }
}

radioFormInput.forEach((e) => {
    e.addEventListener('click', toggleForm);
});

const confirmAddUserBtn = document.getElementById("confirmAddUserBtn"),
    cancelAddUserBtn = document.getElementById("cancelBtnAddUser"),
    addUserBtn = document.getElementById("addUserBtn"),
    deleteUserBtn = document.querySelectorAll(".deleteUserBtn"),
    confirmDeleteUserBtn = document.getElementById("confirmDeleteUserBtn"),
    cancelDeleteUserBtn = document.getElementById("cancelDeleteUserBtn"),
    editUserBtn = document.querySelectorAll(".editUserBtn");

addUserBtn.addEventListener("click", function () {
    document.getElementsByClassName("add_user_page-title")[0].classList.add("active-title")
    document.getElementsByClassName("add_user_page-title")[1].classList.remove("active-title")
    document.getElementsByClassName("add_user_page")[0].classList.add("user_page_active")
    document.getElementById("action").value = "add"
})


confirmAddUserBtn.addEventListener("click", function () {
//    submit form
    let form = document.getElementById("addUserForm");
    form.submit();

})

cancelAddUserBtn.addEventListener("click", function () {
    document.getElementsByClassName("add_user_page")[0].classList.remove("user_page_active")
    document.getElementById("userId").value = null;
    document.getElementById("userFullName").value = null;
    document.getElementById("userPassword").value = null;
    document.getElementById("contact").value = null;
    document.getElementById("address").value = null;
    document.getElementById("emergencyContact").value = null;
    document.getElementById("hospital").value = null;
    document.getElementById("doctorPosition").value = null;
    document.getElementById("sensorId").value = null;
    document.getElementById("userId").readOnly = false;
    document.getElementById("userPassword").readOnly = false;
})

cancelAddUserBtn.addEventListener("click", activeRadioForm)


deleteUserBtn.forEach((e) => {
    e.addEventListener("click", function () {
        document.getElementsByClassName("confirmation_deleteUser_page")[0].classList.add("user_page_active")
        //assigning the id value to the hidden form
        document.getElementById("userIdToBeDelete").value = this.closest("tr").
        getElementsByTagName("td")[0].innerText;
        document.getElementById("userRoleToBeDelete").value = this.closest("tr").
        getElementsByTagName("td")[4].innerText;
    });
});

confirmDeleteUserBtn.addEventListener("click", function () {
//    submit form
    document.getElementById("deleteUserForm").submit();
})
cancelDeleteUserBtn.addEventListener("click", function () {
    document.getElementsByClassName("confirmation_deleteUser_page")[0].classList.remove("user_page_active")
    document.getElementById("userIdToBeDelete").value = ""
})



editUserBtn.forEach((e) => {
    e.addEventListener("click", hideRadioForm)
    e.addEventListener("click", function () {

        for (i = 0; i < userForm.length; i++) {
            userForm[i].className = 'extraForm form-group';
        }

        let editClassName = this.className
        let row = this.closest("tr")
        let cells = row.getElementsByTagName("td")

        document.getElementById("userId").value = cells[0].innerText;
        document.getElementById("userId").readOnly = true;
        document.getElementById("userFullName").value = cells[1].innerText;
        document.getElementById("userPassword").value = cells[3].innerText;
        document.getElementById("contact").value = cells[2].innerText;

        if (editClassName === 'btn btn-warning editUserBtn editPatient') {
            //        if user click edit button on patient table
            userForm[0].className = "extraForm form-group activeForm";
            radioFormInput[1].checked = true;
            document.getElementById("address").value = cells[6].innerText;
            document.getElementById("emergencyContact").value = cells[5].innerText;
            document.getElementById("sensorId").value = cells[7].innerText;

        } else if (editClassName === 'btn btn-warning editUserBtn editDoctor') {
            //        if user click edit button on doctor table
            userForm[1].className = "extraForm form-group activeForm";
            radioFormInput[2].checked = true;
            document.getElementById("hospital").value = cells[5].innerText;
            document.getElementById("doctorPosition").value = cells[6].innerText;
        }else{
            radioFormInput[0].checked = true;
        }
        document.getElementsByClassName("add_user_page-title")[1].classList.add("active-title")
        document.getElementsByClassName("add_user_page-title")[0].classList.remove("active-title")
        document.getElementsByClassName("add_user_page")[0].classList.add("user_page_active")
        document.getElementById("action").value = "update"
    });
});

const radioFormBtn = document.querySelectorAll('.radioBtn')
function hideRadioForm() {

    for (i = 0; i < radioFormBtn.length; i++) {
        radioFormBtn[i].classList.add("radioBtn_hide");
    }
}

function activeRadioForm(){
    for (i = 0; i < radioFormBtn.length; i++) {
        radioFormBtn[i].classList.remove("radioBtn_hide");
    }
}