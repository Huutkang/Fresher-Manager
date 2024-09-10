document.addEventListener("DOMContentLoaded", async function() {
    try {
        // Kiểm tra trạng thái đăng nhập
        const isLoggedIn = await checkLogin();
        if (!isLoggedIn) {
            console.log('Chưa đăng nhập, điều hướng đến trang đăng nhập...');
            window.location.href = '/login';
            return;
        }

        console.log('Đã đăng nhập.');

        // Gọi các hàm API để lấy dữ liệu
        const allUsersResponse = await getAllUsers();
        const allFreshersResponse = await getAllFreshers();
        const fresherStatisticsResponse = await getFresherStatistics();

        // Cập nhật nội dung vào các phần tử tương ứng
        updateContent('get-all-users', allUsersResponse.result, formatUser);
        updateContent('get-all-freshers', allFreshersResponse.result, formatFresher);
        updateContent('fresher-statistics', fresherStatisticsResponse.result, formatStatistics);

    } catch (error) {
        console.error('Lỗi khi lấy dữ liệu:', error);
    }
});

async function logout() {
    try {
        // Gọi API logout nếu có
        await logoutUser();

        // Sau khi logout, điều hướng đến trang đăng nhập
        window.location.href = '/login';
    } catch (error) {
        console.error('Lỗi khi đăng xuất:', error);
    }
}

async function sendemail() {
    const to = document.getElementById('to').value;
    const subject = document.getElementById('subject').value;
    const body = document.getElementById('body').value;
    console.log('Gửi email tới:', to, 'với tiêu đề:', subject, 'và nội dung:', body);
    try {
        const response = await sendEmail(to, subject, body);
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
    const container = document.getElementById(elementId);
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
