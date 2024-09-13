document.addEventListener("DOMContentLoaded", async function() {
    try {
        // Kiểm tra trạng thái đăng nhập
        var isLoggedIn = await checkLogin();
        if (!isLoggedIn) {
            console.log('Chưa đăng nhập, điều hướng đến trang đăng nhập...');
            window.location.href = '/login';
            return;
        }

        console.log('Đã đăng nhập.');

    } catch (error) {
        console.error('Có lỗi xảy ra:');
    }
});

async function sendemail() {
    var to = document.getElementById('to').value;
    var subject = document.getElementById('subject').value;
    var body = document.getElementById('body').value;
    console.log('Gửi email tới:', to, 'với tiêu đề:', subject, 'và nội dung:', body);
    try {
        var response = await sendEmail(to, subject, body);
        console.log('Kết quả:', response);
        if (response && response.code == 1000) {
            alert('Email đã được gửi thành công!');
        } else {
            console.error('Đáp ứng không thành công:', response);
            alert('Lỗi khi gửi email.');
        }
    } catch (error) {
        console.error('Lỗi khi gửi email:', error);
        alert('Có lỗi xảy ra khi gửi email.');
    }
}

function updateContent(elementId, data, formatFunction) {
    var container = document.getElementById(elementId);
    if (data && data.length > 0) {
        container.innerHTML = data.map(formatFunction).join('');
    } else {
        container.innerHTML = '<p>Không có dữ liệu.</p>';
    }
}



// Hàm cho refresh token
async function rfTk() {
    var token = await refreshToken(); // Lấy token mới
    var rftk_result = document.getElementById('rftk_result');
    var p = document.createElement("p");
    p.textContent = "Đã làm mới token:  " +  token;
    rftk_result.appendChild(p);
}

// Hàm trống để kiểm tra token (bạn có thể cập nhật sau)
function checkToken() {
    var inputToken = document.getElementById('token_input').value;
    console.log("Token kiểm tra: ", inputToken);
    // Cập nhật nội dung kiểm tra token ở đây
}


// Hàm kiểm tra trạng thái đăng nhập
async function cLg() {
    var dk = await checkLogin()
    var checklogin = document.getElementById('checklogin_result');
    if (dk) {
        checklogin.innerHTML = '<p>Token hợp lệ. bạn đang đăng nhập</p>';
    } else {
        checklogin.innerHTML = '<p>Token không hợp lệ. bạn đã đăng xuất</p>';
    }
}

function printJsonToElement(elementId, jsonData) {
    // Lấy thẻ HTML dựa vào id
    const element = document.getElementById(elementId);
    
    if (element) {
        // Chuyển đối tượng JSON thành chuỗi có định dạng đẹp
        const formattedJson = JSON.stringify(jsonData, null, 2);

        // In chuỗi JSON vào thẻ HTML dưới dạng preformatted text
        element.innerHTML = `<pre>${formattedJson}</pre>`;
    } else {
        console.error(`Element with id "${elementId}" not found.`);
    }
}

// --------------------------------------------------

// Hàm thêm mới user
async function addnewuser() {
    // Lấy form và các trường trong form
    const form = document.getElementById('add-user-form');
    const formData = new FormData(form);
    const user = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        // Chỉ thêm vào JSON nếu giá trị không rỗng
        if (value.trim() !== '') {
            user[key] = value.trim();
        }
    });
    var kq = await createUser(user);
    printJsonToElement("add-new-user-result", kq);
}

// Hàm lấy tất cả users
async function getalllusers() {
    var alluser = await getAllUsers();
    var alluser_result = document.getElementById('get-all-users');

    console.log('Danh sách tất cả user:', alluser.result);

    // Khởi tạo chuỗi HTML để hiển thị người dùng
    var userListHTML = "<ul>"; // Dùng thẻ <ul> để tạo danh sách người dùng

    alluser.result.forEach(user => {
        userListHTML += "<li>";
        
        // Duyệt qua tất cả các thuộc tính của đối tượng user
        for (const [key, value] of Object.entries(user)) {
            if (Array.isArray(value)) {
                // Nếu giá trị là mảng, nối các phần tử bằng dấu phẩy
                userListHTML += `${key}: ${value.join(', ')}, `;
            } else {
                userListHTML += `${key}: ${value}, `;
            }
        }

        userListHTML = userListHTML.slice(0, -2); // Loại bỏ dấu phẩy cuối cùng
        userListHTML += "</li>";
    });

    userListHTML += "</ul>"; // Kết thúc danh sách

    // Cập nhật nội dung của div 'get-all-users'
    alluser_result.innerHTML = userListHTML;
}

// Hàm lấy user theo ID
async function getuserbyid() {
    // Lấy giá trị của id từ thẻ input
    var userIdInput = document.getElementById('userId');
    var id = userIdInput.value;
    console.log(id)
    if (id) {
        try {
            // Gọi API hoặc hàm lấy user theo id
            var kq = await getUserById(id);
            // In kết quả JSON vào thẻ 'user-id-result'
            printJsonToElement("user-id-result", kq);
        } catch (error) {
            console.error('Error fetching user:', error);
        }
    } else {
        console.log('Please provide a valid user ID.');
    }
}

// Hàm cập nhật user theo ID
async function updateuser() {
    // Lấy form và các trường trong form
    const form = document.getElementById('update-user-form'); // Cập nhật ID form
    const formData = new FormData(form);
    const user = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        // Chỉ thêm vào JSON nếu giá trị không rỗng
        if (value.trim() !== '') {
            user[key] = value.trim();
        }
    });

    // Lấy ID người dùng từ trường input 'userIdUpdate'
    const userId = document.getElementById('userIdUpdate').value;
    console.log(user);
    // Gọi hàm updateUser với id và user data
    var kq = await updateUser(userId, user);
    printJsonToElement("user-update-id-result", kq);
}


// Hàm xóa user theo ID
async function deleteuser() {
    var userIdInput = document.getElementById('userIdDelete');
    var id = userIdInput.value;
    if (id) {
        try {
            var kq = await deleteUser(id);
            printJsonToElement("user-delete-id-result", kq);
        } catch (error) {
            console.error('Error fetching user:', error);
        }
    } else {
        console.log('Please provide a valid user ID.');
    }

}

// Hàm lấy thông tin user hiện tại
async function getuserme() {
    var kq = await getUserMe();
    console.log(kq);
    printJsonToElement("get-me-result", kq);

}

// Hàm cập nhật user hiện tại
async function updateuserme() {
    // Lấy form và các trường trong form
    const form = document.getElementById('update-me-form'); // Cập nhật ID form
    const formData = new FormData(form);
    const user = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        // Chỉ thêm vào JSON nếu giá trị không rỗng
        if (value.trim() !== '') {
            user[key] = value.trim();
        }
    });
    console.log(user);
    // Gọi hàm updateUser với id và user data
    var kq = await updateUserMe(user);
    printJsonToElement("update-me-result", kq);
}

// Thêm role cho User
async function addroletouser() {
    const userId = document.getElementById('userIdRole').value;
    const role = document.getElementById('addrole').value;

    if (userId && role) {
        try {
            const kq = await addRoleToUser(userId, role);
            printJsonToElement("add-role-result", kq);
        } catch (error) {
            console.error('Error adding role:', error);
        }
    } else {
        console.log('Please provide valid user ID and role.');
    }
}


// Xóa role khỏi User
async function removerolefromuser() {
    const userId = document.getElementById('userIdRemoveRole').value;
    const role = document.getElementById('roleRemove').value;

    if (userId && role) {
        try {
            const kq = await removeRoleFromUser(userId, role);
            printJsonToElement("remove-role-result", kq);
        } catch (error) {
            console.error('Error removing role:', error);
        }
    } else {
        console.log('Please provide valid user ID and role.');
    }
}

// Đặt mật khẩu cho người dùng khác
async function setpassword() {
    const userId = document.getElementById('userIdPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    if (userId && newPassword) {
        try {
            const kq = await setPassword(userId, newPassword);
            printJsonToElement("set-password-result", kq);
        } catch (error) {
            console.error('Error setting password:', error);
        }
    } else {
        console.log('Please provide valid user ID and password.');
    }
}

// Cập nhật mật khẩu User
async function updatepassword() {
    const newPassword = document.getElementById('newPasswordMe').value;

    if (newPassword) {
        try {
            const kq = await updatePassword(newPassword);
            printJsonToElement("update-password-result", kq);
        } catch (error) {
            console.error('Error updating password:', error);
        }
    } else {
        console.log('Please provide a new password.');
    }
}

// --------------------------------------------------

// Hàm thêm mới fresher
async function addnewfresher() {
    // Lấy form và các trường trong form
    const form = document.getElementById('add-fresher-form');
    const formData = new FormData(form);
    const fresher = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        // Chỉ thêm vào JSON nếu giá trị không rỗng
        if (value.trim() !== '') {
            fresher[key] = value.trim();
        }
    });

    var kq = await createFresher(fresher);
    printJsonToElement("new-fresher-result", kq);
}

async function addfresher() {
    var fresherIdInput = document.getElementById('addfre');
    var id = fresherIdInput.value;
    if (id) {
        try {
            var kq = await addFresher(id);
            printJsonToElement("addfre-result", kq);
        } catch (error) {
            console.error('Error fetching fresher:', error);
        }
    } else {
        console.log('Please provide a valid fresher ID.');
    }
}


// Hàm lấy tất cả freshers
async function getallfreshers() {
    var allfreshers = await getAllFreshers();
    console.log('Danh sách tất cả fresher:', allfreshers.result);
    printJsonToElement('get-all-prj', allfreshers.result);
}

// Hàm lấy fresher theo ID
async function getfresherbyid() {
    var fresherIdInput = document.getElementById('gfId');
    var id = fresherIdInput.value;
    if (id) {
        try {
            var kq = await getFresherById(id);
            printJsonToElement("fresher-id-result", kq);
        } catch (error) {
            console.error('Error fetching fresher:', error);
        }
    } else {
        console.log('Please provide a valid fresher ID.');
    }
}

// Hàm cập nhật fresher theo ID
async function updatefresher() {
    const form = document.getElementById('update-fresher-form');
    const formData = new FormData(form);
    const fresher = {};

    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            fresher[key] = value.trim();
        }
    });

    const id = document.getElementById('udt-fre-id').value;
    if (id) {
        var kq = await updateFresher(id, fresher);
        printJsonToElement("fresher-update-id-result", kq);
    } else {
        console.log('Please provide a valid fresher ID.');
    }
}

// Hàm xóa fresher theo ID
async function deletefresher() {
    var fresherIdInput = document.getElementById('fresherIdDelete');
    var id = fresherIdInput.value;
    if (id) {
        try {
            var kq = await deleteFresher(id);
            printJsonToElement("fresher-delete-id-result", kq);
        } catch (error) {
            console.error('Error deleting fresher:', error);
        }
    } else {
        console.log('Please provide a valid fresher ID.');
    }
}

// Hàm lấy thông tin fresher hiện tại
async function getfresherme() {
    var kq = await getFresherMe();
    printJsonToElement("get-me-fresher-result", kq);
}

// Hàm cập nhật fresher hiện tại
async function updatefresherme() {
    const form = document.getElementById('update-me-fresher-form');
    const formData = new FormData(form);
    const fresher = {};

    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            fresher[key] = value.trim();
        }
    });
    console.log(fresher);
    var kq = await updateFresherMe(fresher);
    printJsonToElement("update-me-fresher-result", kq);
}

// --------------------------------------------------

// Hàm thêm mới project
async function addnewproject() {
    // Lấy form và các trường trong form
    const form = document.getElementById('add-project-form');
    const formData = new FormData(form);
    const project = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        // Chỉ thêm vào JSON nếu giá trị không rỗng
        if (value.trim() !== '') {
            project[key] = value.trim();
        }
    });

    var kq = await createProject(project);
    console.log(kq);
    printJsonToElement("add-project-result", kq);
}

// Hàm lấy tất cả projects
async function getallprojects() {
    var allprojects = await getAllProjects();
    console.log('Danh sách tất cả projects:', allprojects.result);
    printJsonToElement("get-all-project", allprojects.result);
}

// Hàm lấy project theo ID
async function getprojectbyid() {
    var projectIdInput = document.getElementById('prjbId');
    var id = projectIdInput.value;
    if (id) {
        try {
            var kq = await getProjectById(id);
            printJsonToElement("get-prj", kq);
        } catch (error) {
            console.error('Error fetching project:', error);
        }
    } else {
        console.log('Please provide a valid project ID.');
    }
}

// Hàm cập nhật project theo ID
async function updateproject() {
    const form = document.getElementById('update-project-form');
    const formData = new FormData(form);
    const project = {};

    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            project[key] = value.trim();
        }
    });

    const projectId = document.getElementById('prjidu').value;
    var kq = await updateProject(projectId, project);
    printJsonToElement("update-prj", kq);
}

// Hàm xóa project theo ID
async function deleteproject() {
    var projectIdInput = document.getElementById('projectIdDelete');
    var id = projectIdInput.value;
    if (id) {
        try {
            var kq = await deleteProject(id);
            printJsonToElement("delete-prj", kq);
        } catch (error) {
            console.error('Error deleting project:', error);
        }
    } else {
        console.log('Please provide a valid project ID.');
    }
}

// --------------------------------------------------
// Hàm thêm mới Center
async function addnewcenter() {
    // Lấy form và các trường trong form
    const form = document.getElementById('add-center-form');
    const formData = new FormData(form);
    const center = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            center[key] = value.trim();
        }
    });

    try {
        var kq = await createCenter(center);
        console.log(kq);
        printJsonToElement("add-center-result", kq);
    } catch (error) {
        console.error('Error adding center:', error);
    }
}

// Hàm lấy tất cả Centers
async function getallcenters() {
    try {
        var allCenters = await getAllCenters();
        console.log('Danh sách tất cả centers:', allCenters.result);
        printJsonToElement("centers-list", allCenters.result);
    } catch (error) {
        console.error('Error fetching centers:', error);
    }
}

// Hàm lấy Center theo ID
async function getcenterbyid() {
    var centerIdInput = document.getElementById('centerId');
    var id = centerIdInput.value;
    if (id) {
        try {
            var kq = await getCenterById(id);
            printJsonToElement("center-info", kq);
        } catch (error) {
            console.error('Error fetching center:', error);
        }
    } else {
        console.log('Please provide a valid center ID.');
    }
}

// Hàm cập nhật Center theo ID
async function updatecenter() {
    const form = document.getElementById('update-center-form');
    const formData = new FormData(form);
    const center = {};

    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            center[key] = value.trim();
        }
    });

    const centerId = document.getElementById('centerIdUpdate').value;
    if (centerId) {
        try {
            var kq = await updateCenter(centerId, center);
            printJsonToElement("update-center-result", kq);
        } catch (error) {
            console.error('Error updating center:', error);
        }
    } else {
        console.log('Please provide a valid center ID.');
    }
}

// Hàm xóa Center theo ID
async function deletecenter() {
    var centerIdInput = document.getElementById('centerIdDelete');
    var id = centerIdInput.value;
    if (id) {
        try {
            var kq = await deleteCenter(id);
            printJsonToElement("delete-center-result", kq);
        } catch (error) {
            console.error('Error deleting center:', error);
        }
    } else {
        console.log('Please provide a valid center ID.');
    }
}

// --------------------------------------------------

// Hàm thêm mới Assignment
async function addnewassignment() {
    // Lấy form và các trường trong form
    const form = document.getElementById('add-assignment-form');
    const formData = new FormData(form);
    const assignment = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            assignment[key] = value.trim();
        }
    });

    try {
        var kq = await createAssignment(assignment);
        console.log(kq);
        printJsonToElement("add-assignment-result", kq);
    } catch (error) {
        console.error('Error adding assignment:', error);
    }
}

// Hàm lấy tất cả Assignments
async function getallassignments() {
    try {
        var allAssignments = await getAllAssignments();
        console.log('Danh sách tất cả assignments:', allAssignments.result);
        printJsonToElement("assignments-list", allAssignments.result);
    } catch (error) {
        console.error('Error fetching assignments:', error);
    }
}

// Hàm lấy Assignment theo ID
async function getassignmentbyid() {
    var assignmentIdInput = document.getElementById('assignmentId');
    var id = assignmentIdInput.value;
    if (id) {
        try {
            var kq = await getAssignmentById(id);
            printJsonToElement("assignment-info", kq);
        } catch (error) {
            console.error('Error fetching assignment:', error);
        }
    } else {
        console.log('Please provide a valid assignment ID.');
    }
}

// Hàm cập nhật Assignment theo ID
async function updateassignment() {
    const form = document.getElementById('update-assignment-form');
    const formData = new FormData(form);
    const assignment = {};

    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            assignment[key] = value.trim();
        }
    });

    const assignmentId = document.getElementById('assignmentIdUpdate').value;
    if (assignmentId) {
        try {
            var kq = await updateAssignment(assignmentId, assignment);
            printJsonToElement("update-assignment-result", kq);
        } catch (error) {
            console.error('Error updating assignment:', error);
        }
    } else {
        console.log('Please provide a valid assignment ID.');
    }
}

// Hàm xóa Assignment theo ID
async function deleteassignment() {
    var assignmentIdInput = document.getElementById('assignmentIdDelete');
    var id = assignmentIdInput.value;
    if (id) {
        try {
            var kq = await deleteAssignment(id);
            printJsonToElement("delete-assignment-result", kq);
        } catch (error) {
            console.error('Error deleting assignment:', error);
        }
    } else {
        console.log('Please provide a valid assignment ID.');
    }
}

// --------------------------------------------------

// Hàm thêm mới Fresher Project
async function addnewfresherproject() {
    // Lấy form và các trường trong form
    const form = document.getElementById('add-fresherProject-form');
    const formData = new FormData(form);
    const fresherProject = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            fresherProject[key] = value.trim();
        }
    });

    try {
        var kq = await createFresherProject(fresherProject);
        console.log(kq);
        printJsonToElement("add-fresherProject-result", kq);
    } catch (error) {
        console.error('Error adding fresher project:', error);
    }
}

// Hàm lấy tất cả Fresher Projects
async function getallfresherprojects() {
    try {
        var allFresherProjects = await getAllFresherProjects();
        console.log('Danh sách tất cả fresher projects:', allFresherProjects.result);
        printJsonToElement("fresherProjects-list", allFresherProjects.result);
    } catch (error) {
        console.error('Error fetching fresher projects:', error);
    }
}

// Hàm lấy Fresher Project theo ID
async function getfresherprojectbyid() {
    var fresherProjectIdInput = document.getElementById('fresherProjectId');
    var id = fresherProjectIdInput.value;
    if (id) {
        try {
            var kq = await getFresherProjectById(id);
            printJsonToElement("fresherProject-info", kq);
        } catch (error) {
            console.error('Error fetching fresher project:', error);
        }
    } else {
        console.log('Please provide a valid fresher project ID.');
    }
}

// Hàm cập nhật Fresher Project theo ID
async function updatefresherproject() {
    const form = document.getElementById('update-fresherProject-form');
    const formData = new FormData(form);
    const fresherProject = {};

    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            fresherProject[key] = value.trim();
        }
    });

    const fresherProjectId = document.getElementById('fresherProjectIdUpdate').value;
    if (fresherProjectId) {
        try {
            var kq = await updateFresherProject(fresherProjectId, fresherProject);
            printJsonToElement("update-fresherProject-result", kq);
        } catch (error) {
            console.error('Error updating fresher project:', error);
        }
    } else {
        console.log('Please provide a valid fresher project ID.');
    }
}

// Hàm xóa Fresher Project theo ID
async function deletefresherproject() {
    var fresherProjectIdInput = document.getElementById('fresherProjectIdDelete');
    var id = fresherProjectIdInput.value;
    if (id) {
        try {
            var kq = await deleteFresherProject(id);
            printJsonToElement("delete-fresherProject-result", kq);
        } catch (error) {
            console.error('Error deleting fresher project:', error);
        }
    } else {
        console.log('Please provide a valid fresher project ID.');
    }
}

// --------------------------------------------------

// Hàm thêm mới Notification
async function addnewnotification() {
    const form = document.getElementById('add-notification-form');
    const formData = new FormData(form);
    const notification = {};

    // Duyệt qua các trường dữ liệu của form
    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            notification[key] = value.trim();
        }
    });

    try {
        var kq = await createNotification(notification);
        console.log(kq);
        printJsonToElement("add-notification-result", kq);
    } catch (error) {
        console.error('Error adding notification:', error);
    }
}

// Hàm lấy tất cả Notifications
async function getallnotifications() {
    try {
        var allNotifications = await getAllNotifications();
        console.log('Danh sách tất cả notifications:', allNotifications.result);
        printJsonToElement("notifications-list", allNotifications.result);
    } catch (error) {
        console.error('Error fetching notifications:', error);
    }
}

// Hàm lấy Notification theo ID
async function getnotificationbyid() {
    var notificationIdInput = document.getElementById('notificationId');
    var id = notificationIdInput.value;
    if (id) {
        try {
            var kq = await getNotificationById(id);
            printJsonToElement("notification-info", kq);
        } catch (error) {
            console.error('Error fetching notification:', error);
        }
    } else {
        console.log('Please provide a valid notification ID.');
    }
}

// Hàm cập nhật Notification theo ID
async function updatenotification() {
    const form = document.getElementById('update-notification-form');
    const formData = new FormData(form);
    const notification = {};

    formData.forEach((value, key) => {
        if (value.trim() !== '') {
            notification[key] = value.trim();
        }
    });

    const notificationId = document.getElementById('notificationIdUpdate').value;
    if (notificationId) {
        try {
            var kq = await updateNotification(notificationId, notification);
            printJsonToElement("update-notification-result", kq);
        } catch (error) {
            console.error('Error updating notification:', error);
        }
    } else {
        console.log('Please provide a valid notification ID.');
    }
}

// Hàm xóa Notification theo ID
async function deletenotification() {
    var notificationIdInput = document.getElementById('notificationIdDelete');
    var id = notificationIdInput.value;
    if (id) {
        try {
            var kq = await deleteNotification(id);
            printJsonToElement("delete-notification-result", kq);
        } catch (error) {
            console.error('Error deleting notification:', error);
        }
    } else {
        console.log('Please provide a valid notification ID.');
    }
}

// --------------------------------------------------

// Tìm kiếm Fresher theo tiêu chí
async function searchfresher() {
    const keywordsInput = document.getElementById('keywords');
    const keywords = keywordsInput.value.trim();
    if (keywords) {
        try {
            const kq = await searchFresher(keywords);
            printJsonToElement("fresher-search-result", kq);
        } catch (error) {
            console.error('Error searching fresher:', error);
        }
    } else {
        console.log('Please provide valid keywords.');
    }
}

// Tìm kiếm Fresher theo tiêu chí
async function smartsearchfresher() {
    const keywordsInput = document.getElementById('sms');
    const keywords = keywordsInput.value.trim();
    if (keywords) {
        try {
            const kq = await smartSearchFresher(keywords);
            printJsonToElement("fsr", kq);
        } catch (error) {
            console.error('Error searching fresher:', error);
        }
    } else {
        console.log('Please provide valid keywords.');
    }
}

// Tìm kiếm trung tâm theo tên
async function searchcenterbyname() {
    const centerNameInput = document.getElementById('scbn');
    const centerName = centerNameInput.value.trim();
    if (centerName) {
        try {
            const kq = await searchCenterByName(centerName);
            printJsonToElement("center-search-result", kq);
        } catch (error) {
            console.error('Error searching center:', error);
        }
    } else {
        console.log('Please provide a valid center name.');
    }
}

// Tìm kiếm dự án theo tên
async function searchprojectbyname() {
    const projectNameInput = document.getElementById('spbn');
    const projectName = projectNameInput.value.trim();
    console.log(projectName);
    if (projectName) {
        try {
            const kq = await searchProjectByName(projectName);
            printJsonToElement("project-search-result", kq);
        } catch (error) {
            console.error('Error searching project:', error);
        }
    } else {
        console.log('Please provide a valid project name.');
    }
}

// Tìm kiếm các dự án của Fresher dựa trên fresherId
async function searchprojectsbyfresherid() {
    const fresherIdInput = document.getElementById('tkdafre');
    const fresherId = fresherIdInput.value.trim();
    console.log(fresherId);
    if (fresherId) {
        try {
            const kq = await searchProjectsByFresherId(fresherId);
            printJsonToElement("fresher-projects-search-result", kq);
        } catch (error) {
            console.error('Error searching projects by fresher ID:', error);
        }
    } else {
        console.log('Please provide a valid fresher ID.');
    }
}

// --------------------------------------------------

async function countobjects(){
    var keywords = document.getElementById('object');
    var object = keywords.value;
    var kq = await count(object);
    printJsonToElement("object-count-result", kq.result);
}

// Tính điểm trung bình của Fresher dựa trên fresher_id
async function calculateaveragescore() {
    const fresherIdInput = document.getElementById('cfasbofi');
    const fresherId = fresherIdInput.value.trim();
    if (fresherId) {
        try {
            const kq = await averageScore(fresherId);
            printJsonToElement("average-score-result", kq);
        } catch (error) {
            console.error('Error calculating average score:', error);
        }
    } else {
        console.log('Please provide a valid fresher ID.');
    }
}

// Tính điểm của Fresher dựa trên fresher_id
async function calculatescore() {
    const fresherIdInput = document.getElementById('fresherIdScore');
    const fresherId = fresherIdInput.value.trim();
    if (fresherId) {
        try {
            const kq = await score(fresherId);
            printJsonToElement("score-result", kq);
        } catch (error) {
            console.error('Error calculating score:', error);
        }
    } else {
        console.log('Please provide a valid fresher ID.');
    }
}

// Lấy số lượng Fresher theo từng trung tâm
async function getfresherbycenter() {
    try {
        const kq = await fresherByCenter();
        printJsonToElement("fresher-by-center-result", kq);
    } catch (error) {
        console.error('Error getting fresher by center:', error);
    }
}

// Lấy số lượng Fresher theo điểm số
async function getfresherbyscore() {
    try {
        const kq = await fresherByScore();
        printJsonToElement("fresher-by-score-result", kq);
    } catch (error) {
        console.error('Error getting fresher by score:', error);
    }
}

// Lấy số lượng Fresher theo điểm chữ (grade)
async function getfresherbygrade() {
    try {
        const kq = await fresherByGrade();
        printJsonToElement("fresher-by-grade-result", kq);
    } catch (error) {
        console.error('Error getting fresher by grade:', error);
    }
}

// Lấy toàn bộ các thống kê liên quan đến fresher
async function getfresherstatistics() {
    try {
        const kq = await getFresherStatistics();
        printJsonToElement("fresher-statistics-result", kq);
    } catch (error) {
        console.error('Error getting fresher statistics:', error);
    }
}
