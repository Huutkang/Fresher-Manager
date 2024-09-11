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
    // TODO: Cập nhật thông tin user theo ID
}

// Hàm xóa user theo ID
async function deleteuser() {
    // TODO: Xóa user theo ID
}

// Hàm lấy thông tin user hiện tại
async function getuserme() {
    // TODO: Lấy thông tin user hiện tại
}

// Hàm cập nhật user hiện tại
async function updateuserme() {
    // TODO: Cập nhật thông tin user hiện tại
}

// Hàm thêm mới fresher
async function addNewFresher() {
    // TODO: Thêm mới fresher
}

// Hàm lấy tất cả freshers
async function getAllFreshers() {
    // TODO: Lấy danh sách tất cả freshers
}

// Hàm lấy fresher theo ID
async function getFresherById() {
    // TODO: Lấy thông tin fresher theo ID
}

// Hàm cập nhật fresher theo ID
async function updateFresher() {
    // TODO: Cập nhật thông tin fresher theo ID
}

// Hàm xóa fresher theo ID
async function deleteFresher() {
    // TODO: Xóa fresher theo ID
}

// Hàm lấy thông tin fresher hiện tại
async function getFresherMe() {
    // TODO: Lấy thông tin fresher hiện tại
}

// Hàm cập nhật fresher hiện tại
async function updateFresherMe() {
    // TODO: Cập nhật thông tin fresher hiện tại
}

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