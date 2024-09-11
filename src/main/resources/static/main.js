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

        // Gọi các hàm API để lấy dữ liệu
        var allUsersResponse = await getAllUsers();
        var allFreshersResponse = await getAllFreshers();
        var fresherStatisticsResponse = await getFresherStatistics();

        // Cập nhật nội dung vào các phần tử tương ứng
        updateContent('get-all-users', allUsersResponse.result, formatUser);
        updateContent('get-all-freshers', allFreshersResponse.result, formatFresher);
        updateContent('fresher-statistics', fresherStatisticsResponse.result, formatStatistics);

    } catch (error) {
        console.error('Lỗi khi lấy dữ liệu:', error);
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

function formatUser(user) {
    return `<p>ID: ${user.id}, Tên: ${user.name}, Email: ${user.email || 'N/A'}, Vai trò: ${user.roles.join(', ')}</p>`;
}

function formatFresher(fresher) {
    return `<p>ID: ${fresher.id}, Tên: ${fresher.name}, Ngôn ngữ lập trình: ${fresher.programmingLanguage || 'N/A'}</p>`;
}

function formatStatistics(statistic) {
    return `<p>${statistic.key}: ${statistic.value}</p>`;
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
function addNewUser() {
    // TODO: Thêm mới user
}

// Hàm lấy tất cả users
function getAllUsers() {
    // TODO: Lấy danh sách tất cả users
}

// Hàm lấy user theo ID
function getUserById() {
    // TODO: Lấy thông tin user theo ID
}

// Hàm cập nhật user theo ID
function updateUser() {
    // TODO: Cập nhật thông tin user theo ID
}

// Hàm xóa user theo ID
function deleteUser() {
    // TODO: Xóa user theo ID
}

// Hàm lấy thông tin user hiện tại
function getUserMe() {
    // TODO: Lấy thông tin user hiện tại
}

// Hàm cập nhật user hiện tại
function updateUserMe() {
    // TODO: Cập nhật thông tin user hiện tại
}

// Hàm thêm mới fresher
function addNewFresher() {
    // TODO: Thêm mới fresher
}

// Hàm lấy tất cả freshers
function getAllFreshers() {
    // TODO: Lấy danh sách tất cả freshers
}

// Hàm lấy fresher theo ID
function getFresherById() {
    // TODO: Lấy thông tin fresher theo ID
}

// Hàm cập nhật fresher theo ID
function updateFresher() {
    // TODO: Cập nhật thông tin fresher theo ID
}

// Hàm xóa fresher theo ID
function deleteFresher() {
    // TODO: Xóa fresher theo ID
}

// Hàm lấy thông tin fresher hiện tại
function getFresherMe() {
    // TODO: Lấy thông tin fresher hiện tại
}

// Hàm cập nhật fresher hiện tại
function updateFresherMe() {
    // TODO: Cập nhật thông tin fresher hiện tại
}

// Hàm thêm mới project
function addNewProject() {
    // TODO: Thêm mới project
}

// Hàm lấy tất cả projects
function getAllProjects() {
    // TODO: Lấy danh sách tất cả projects
}

// Hàm lấy project theo ID
function getProjectById() {
    // TODO: Lấy thông tin project theo ID
}

// Hàm cập nhật project theo ID
function updateProject() {
    // TODO: Cập nhật thông tin project theo ID
}