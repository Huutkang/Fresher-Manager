// Đặt thời gian hết hạn token là 1 giờ (3600 giây)
const TOKEN_EXPIRE_TIME = 3600 * 1000; // 1 giờ tính bằng milliseconds
const REFRESH_THRESHOLD = 5 * 60 * 1000; // 5 phút trước khi hết hạn

// Thiết lập interval để kiểm tra token mỗi 5 phút
setInterval(checkAndRefreshToken, 5 * 60 * 1000); // Kiểm tra mỗi 5 phút

// Hàm lưu token và thời gian hết hạn vào localStorage
function setToken(token) {
    const currentTime = Date.now();
    const expireTime = currentTime + TOKEN_EXPIRE_TIME; // Token hết hạn sau 1 giờ

    localStorage.setItem('authToken', token);
    localStorage.setItem('expireTime', expireTime.toString()); // Lưu thời gian hết hạn dưới dạng string
}

// Hàm lấy token từ localStorage
function getToken() {
    return localStorage.getItem('authToken');
}

// Hàm xóa token khi đăng xuất
function removeToken() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('expireTime');
}

const baseUrl = 'http://localhost:8081';

async function sendRequest(url, data = null, method = 'POST') {
    const token = getToken(); // Lấy token từ localStorage
    const headers = {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
    };
    const config = {
        method: method,
        headers: headers,
    };
    // Nếu có dữ liệu và phương thức không phải 'GET' hoặc 'DELETE', thêm 'body'
    if (data && method !== 'GET' && method !== 'DELETE') {
        config.body = JSON.stringify(data);
    }
    const response = await fetch(url, config);
    return response.json();
}

async function send(url, data = null, method = 'POST'){
    console.log("Data gửi đi:", data);
    var kq = await sendRequest(url, data, method);
    console.log("Kết quả trả về:", kq);
    return parseJsonData(kq);
}

// Đăng nhập và lưu token vào localStorage
async function login(username, password) {
    removeToken(); // Xóa token cũ khi đăng nhập
    const url = `${baseUrl}/auth/login`;
    const data = {
        "username": username,
        "password": password
    };
    
    const response = await sendRequest(url, data);
    if (response.result && response.result.token) {
        setToken(response.result.token); // Lưu token vào localStorage
    }

    return response;
}

// Hàm checkLogin với phương thức GET hợp lệ
async function checkLogin(){
    const url = `${baseUrl}/auth/checklogin`;
    try{
        const response = await sendRequest(url, method = 'GET');
        console.log("mã code là:", response.code);
        if (response.code == 1002){
            console.log("checkLogin: True");
            return true;
        }else{
            console.log("checkLogin: False");
            return false;
        }
    }catch(error){
        console.error("Lỗi khi kiểm tra đăng nhập:", error);
        return false;
    }
}

// Đăng xuất và xóa token khỏi localStorage
async function logout() {
    const url = `${baseUrl}/auth/logout`;
    await sendRequest(url, null, 'GET');
    removeToken(); // Xóa token khi đăng xuất
    window.location.reload();
}

// Hàm lấy thời gian hết hạn token từ localStorage
function getTokenExpireTime() {
    const expireTime = localStorage.getItem('expireTime');
    return expireTime ? parseInt(expireTime, 10) : null;
}

// Hàm kiểm tra token có sắp hết hạn không
function isTokenExpiringSoon() {
    const expireTime = getTokenExpireTime();
    const currentTime = Date.now();

    if (!expireTime) return false; // Nếu không có expireTime, coi như không cần làm mới

    // Kiểm tra nếu thời gian hiện tại gần đến thời gian hết hạn (dưới 5 phút)
    return (expireTime - currentTime) <= REFRESH_THRESHOLD;
}

// Hàm tự động refresh token khi gần hết hạn
async function checkAndRefreshToken() {
    if (isTokenExpiringSoon()) {
        console.log('Token sắp hết hạn, tiến hành làm mới token...');
        await refreshToken();
    }
}

// Hàm làm mới token được sửa lại
async function refreshToken() {
    const url = `${baseUrl}/auth/refreshtoken`;
    var response = await sendRequest(url, null, 'POST');
    response = parseJsonData(response);

    console.log('Kết quả làm mới token:', response.result.token);

    // Sửa để lấy token từ response.result
    if (response.result.token) {
        setToken(response.result.token);  // Cập nhật token mới
        return response.result.token;
    } else {
        console.log('Không thể làm mới token, đăng xuất...');
        await logout();  // Đăng xuất nếu không thể làm mới token
        window.location.href = '/login';
    }
}


function parseJsonData(json) {
    // Kiểm tra xem json có phải là chuỗi JSON hay không
    if (typeof json === 'string') {
        try {
            json = JSON.parse(json); // Parse chuỗi JSON thành object
        } catch (error) {
            console.error("Chuỗi không hợp lệ:", error);
            return null;
        }
    }

    // Cung cấp giá trị mặc định nếu thiếu
    const defaultCode = -1;
    const defaultMessage = "No message";
    const defaultResult = {};

    return {
        code: json.code !== undefined ? json.code : defaultCode,
        message: json.message !== undefined ? json.message : defaultMessage,
        result: json.result !== undefined ? json.result : defaultResult
    };
}


// --------------------------------------------------

// Thêm User mới
async function createUser(data) {
    const url = `${baseUrl}/users`;
    return await send(url, data);
}

// Lấy tất cả Users
async function getAllUsers() {

    const url = `${baseUrl}/users`;
    return await send(url, null, 'GET');
}

// Lấy User theo ID
async function getUserById(id) {
    const url = `${baseUrl}/users/${id}`;
    return await send(url, null, 'GET');
}

// Cập nhật User theo ID
async function updateUser(id, data) {
    const url = `${baseUrl}/users/${id}`;
    return await send(url, data, 'PUT');
}

// Xóa User theo ID
async function deleteUser(id) {
    const url = `${baseUrl}/users/${id}`;
    return await send(url, null, 'DELETE');
}

async function getUserMe() {
    const url = `${baseUrl}/users/me`;
    return await send(url, null, 'GET');
}

async function updateUserMe(data) {
    const url = `${baseUrl}/users/me`;
    return await send(url, data, 'PUT');
}

// Thêm role cho User
async function addRoleToUser(id, role) {
    const url = `${baseUrl}/users/${id}/addrole`;
    return await send(url, role, 'PUT');
}

// Xóa role khỏi User
async function removeRoleFromUser(id, role) {
    const url = `${baseUrl}/users/${id}/removerole`;
    return await send(url, role, 'PUT');
}

// Cập nhật mật khẩu User
async function setPassword(id, newPassword) {
    const url = `${baseUrl}/users/${id}/setpassword`;
    return await send(url, newPassword, 'PUT');
}

async function updatePassword(newPassword) {
    const url = `${baseUrl}/users/updatepassword`;
    return await send(url, newPassword, 'PUT');
}

// --------------------------------------------------

async function createFresher(fresherData) {
    const url = `${baseUrl}/freshers`;
    return await send(url, fresherData);
}

async function addFresher(id) {
    const url = `${baseUrl}/freshers/${id}`;
    return await send(url);
}

async function getAllFreshers() {
    const url = `${baseUrl}/freshers`;
    return await send(url, null, 'GET');
}

async function getFresherById(id) {
    const url = `${baseUrl}/freshers/${id}`;
    return await send(url, null, 'GET');
}

async function updateFresher(id, fresherData) {
    const url = `${baseUrl}/freshers/${id}`;
    return await send(url, fresherData, 'PUT');
}

async function deleteFresher(id) {
    const url = `${baseUrl}/freshers/${id}`;
    return await send(url, null, 'DELETE');
}

async function getFresherMe() {
    const url = `${baseUrl}/freshers/me`;
    return await send(url, null, 'GET');
}

async function updateFresherMe(data) {
    const url = `${baseUrl}/freshers/me`;
    return await send(url, data, 'PUT');
}

// --------------------------------------------------

// Thêm mới Project
async function createProject(projectData) {
    const url = `${baseUrl}/projects`;
    return await send(url, projectData);
}

async function getAllProjects() {
    const url = `${baseUrl}/projects`;
    return await send(url, null, 'GET');
}

async function getProjectById(id) {
    const url = `${baseUrl}/projects/${id}`;
    return await send(url, null, 'GET');
}

async function updateProject(id, projectDetails) {
    const url = `${baseUrl}/projects/${id}`;
    return await send(url, projectDetails, 'PUT');
}

async function deleteProject(id) {
    const url = `${baseUrl}/projects/${id}`;
    return await send(url, null, 'DELETE');
}

// --------------------------------------------------

// Thêm mới Notification
async function createNotification(notificationData) {
    const url = `${baseUrl}/notifications`;
    return await send(url, notificationData);
}

// Lấy tất cả Notifications
async function getAllNotifications() {
    const url = `${baseUrl}/notifications`;
    return await send(url, null, 'GET');
}

// Lấy Notification theo ID
async function getNotificationById(id) {
    const url = `${baseUrl}/notifications/${id}`;
    return await send(url, null, 'GET');
}

// Cập nhật Notification theo ID
async function updateNotification(id, notificationData) {
    const url = `${baseUrl}/notifications/${id}`;
    return await send(url, notificationData, 'PUT');
}

// Xóa Notification theo ID
async function deleteNotification(id) {
    const url = `${baseUrl}/notifications/${id}`;
    return await send(url, null, 'DELETE');
}

// --------------------------------------------------

// Thêm mới Center
async function createCenter(centerData) {
    const url = `${baseUrl}/centers`;
    return await send(url, centerData);
}

// Lấy tất cả Centers
async function getAllCenters() {
    const url = `${baseUrl}/centers`;
    return await send(url, null, 'GET');
}

// Lấy Center theo ID
async function getCenterById(id) {
    const url = `${baseUrl}/centers/${id}`;
    return await send(url, null, 'GET');
}

// Cập nhật Center theo ID
async function updateCenter(id, centerData) {
    const url = `${baseUrl}/centers/${id}`;
    return await send(url, centerData, 'PUT');
}

// Xóa Center theo ID
async function deleteCenter(id) {
    const url = `${baseUrl}/centers/${id}`;
    return await send(url, null, 'DELETE');
}

// --------------------------------------------------

// Thêm mới FresherProject
async function createFresherProject(fresherProjectData) {
    const url = `${baseUrl}/fresherprojects`;
    return await send(url, fresherProjectData);
}

// Lấy tất cả FresherProjects
async function getAllFresherProjects() {
    const url = `${baseUrl}/fresherprojects`;
    return await send(url, null, 'GET');
}

// Lấy FresherProject theo ID
async function getFresherProjectById(id) {
    const url = `${baseUrl}/fresherprojects/${id}`;
    return await send(url, null, 'GET');
}

// Cập nhật FresherProject theo ID
async function updateFresherProject(id, fresherProjectData) {
    const url = `${baseUrl}/fresherprojects/${id}`;
    return await send(url, fresherProjectData, 'PUT');
}

// Xóa FresherProject theo ID
async function deleteFresherProject(id) {
    const url = `${baseUrl}/fresherprojects/${id}`;
    return await send(url, null, 'DELETE');
}

// --------------------------------------------------

// Thêm mới Assignment
async function createAssignment(assignmentData) {
    const url = `${baseUrl}/assignments`;
    return await send(url, assignmentData);
}

// Lấy tất cả Assignments
async function getAllAssignments() {
    const url = `${baseUrl}/assignments`;
    return await send(url, null, 'GET');
}

// Lấy Assignment theo ID
async function getAssignmentById(id) {
    const url = `${baseUrl}/assignments/${id}`;
    return await send(url, null, 'GET');
}

// Cập nhật Assignment theo ID
async function updateAssignment(id, assignmentData) {
    const url = `${baseUrl}/assignments/${id}`;
    return await send(url, assignmentData, 'PUT');
}

// Xóa Assignment theo ID
async function deleteAssignment(id) {
    const url = `${baseUrl}/assignments/${id}`;
    return await send(url, null, 'DELETE');
}

// --------------------------------------------------

// Tìm kiếm fresher theo các tiêu chí
async function searchFresher(keywords) {
    const url = `${baseUrl}/search/fresher?keywords=${encodeURIComponent(keywords)}`;
    return await send(url, null, 'GET');
}

async function smartSearchFresher(keywords) {
    const url = `${baseUrl}/search/smartsearchfresher?keywords=${encodeURIComponent(keywords)}`;
    return await send(url, null, 'GET');
}

// Tìm kiếm trung tâm theo tên
async function searchCenterByName(name) {
    const url = `${baseUrl}/search/center/${encodeURIComponent(name)}`;
    return await send(url, null, 'GET');
}

// Tìm kiếm dự án theo tên
async function searchProjectByName(name) {
    const url = `${baseUrl}/search/project/${encodeURIComponent(name)}`;
    return await send(url, null, 'GET');
}

// Tìm kiếm các dự án của một fresher dựa trên fresherId
async function searchProjectsByFresherId(fresherId) {
    const url = `${baseUrl}/search/fresher/${fresherId}/projects`;
    return await send(url, null, 'GET');
}

// --------------------------------------------------

// Đếm số lượng đối tượng (fresher hoặc center)
async function count(object) {
    const url = `${baseUrl}/statistics/count`;
    const body = { object: object }; // Đặt dữ liệu vào một đối tượng
    return await send(url, body);
}

// Tính điểm trung bình của fresher dựa trên fresher_id
async function averageScore(fresherId) {
    const url = `${baseUrl}/statistics/average-score/${fresherId}`;
    return await send(url, null, 'GET');
}

// Tính điểm của fresher dựa trên fresher_id
async function score(fresherId) {
    const url = `${baseUrl}/statistics/score/${fresherId}`;
    return await send(url, null, 'GET');
}

// Thống kê số lượng fresher theo từng trung tâm
async function fresherByCenter() {
    const url = `${baseUrl}/statistics/fresher-by-center`;
    return await send(url, null, 'GET');
}

// Thống kê số lượng fresher theo điểm số
async function fresherByScore() {
    const url = `${baseUrl}/statistics/fresher-by-score`;
    return await send(url, null, 'GET');
}

// Thống kê số lượng fresher theo điểm chữ (grade)
async function fresherByGrade() {
    const url = `${baseUrl}/statistics/fresher-by-grade`;
    return await send(url, null, 'GET');
}

// Lấy toàn bộ các thống kê liên quan đến fresher
async function getFresherStatistics() {
    const url = `${baseUrl}/statistics/fresher-statistics`;
    return await send(url, null, 'GET');
}

// --------------------------------------------------

// Gửi email từ tên miền của bạn
async function sendEmail(to, subject, body) {
    const url = `${baseUrl}/email/send`; // URL trỏ đến endpoint của EmailController
    const emailData = {
        to: to,
        subject: subject,
        text: body // Thay 'body' bằng 'text' vì EmailRequest có trường 'text'
    };
    var kq = await sendRequest(url, emailData);
    return parseJsonData(kq)
}
