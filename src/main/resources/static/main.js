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
            printJsonToElement("add-fresher-result", kq);
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
    var allfresher_result = document.getElementById('get-all-freshers');

    console.log('Danh sách tất cả fresher:', allfreshers.result);

    var fresherListHTML = "<ul>"; // Dùng thẻ <ul> để tạo danh sách fresher

    allfreshers.result.forEach(fresher => {
        fresherListHTML += "<li>";

        for (const [key, value] of Object.entries(fresher)) {
            if (Array.isArray(value)) {
                fresherListHTML += `${key}: ${value.join(', ')}, `;
            } else {
                fresherListHTML += `${key}: ${value}, `;
            }
        }

        fresherListHTML = fresherListHTML.slice(0, -2); // Loại bỏ dấu phẩy cuối cùng
        fresherListHTML += "</li>";
    });

    fresherListHTML += "</ul>";

    allfresher_result.innerHTML = fresherListHTML;
}

// Hàm lấy fresher theo ID
async function getfresherbyid() {
    var fresherIdInput = document.getElementById('fresherId');
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

    const fresherId = document.getElementById('fresherIdUpdate').value;
    var kq = await updateFresher(fresherId, fresher);
    printJsonToElement("fresher-update-id-result", kq);
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
async function addNewProject() {
    // TODO: Thêm mới project
}

// Hàm lấy tất cả projects
async function getAllProjects() {
    // TODO: Lấy danh sách tất cả projects
}

// Hàm lấy project theo ID
async function getProjectById() {
    // TODO: Lấy thông tin project theo ID
}

// Hàm cập nhật project theo ID
async function updateProject() {
    // TODO: Cập nhật thông tin project theo ID
}