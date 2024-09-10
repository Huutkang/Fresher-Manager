// Đảm bảo rằng file api.js được import trước khi sử dụng các hàm từ đó
document.addEventListener('DOMContentLoaded', async () => {
    // Gọi hàm checkLogin để kiểm tra trạng thái đăng nhập khi load trang
    try {
        const isLoggedIn = await checkLogin();
        if (isLoggedIn){
            console.log("xxxxxx"+checkLogin());
            window.location.href = '/';
        }
    } catch (error) {
        console.error('Lỗi khi kiểm tra trạng thái đăng nhập:', error);
    }

    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const loginButton = document.getElementById('loginButton');
    const togglePassword = document.getElementById('togglePassword');

    
    // kiểm tra xem cả hai trường đã được điền chưa
    const checkInputs = () => {
        if (usernameInput.value && passwordInput.value) {
            loginButton.disabled = false;
        } else {
            loginButton.disabled = true;
        }
    };

    // Thêm trình xử lý sự kiện vào trường đầu vào
    usernameInput.addEventListener('input', checkInputs);
    passwordInput.addEventListener('input', checkInputs);

    // Hiện ẩn mật khẩu
    togglePassword.addEventListener('click', () => {
        const type = passwordInput.type === 'password' ? 'text' : 'password';
        passwordInput.type = type;
        togglePassword.src = passwordInput.type === 'password' ? 'static/eye-icon.png' : 'static/eye-slash-icon.png';
    });

    // Xử lý việc gửi biểu mẫu
    document.getElementById('loginForm').addEventListener('submit', async (event) => {
        event.preventDefault();

        const username = usernameInput.value;
        const password = passwordInput.value;

        try {
            var a = await login(username, password);
            console.log(a);
            switch (a.code) {
                case 1000:
                case 1001:
                    window.location.href = '/';
                    break;

            }
        } catch (error) {
            alert('Có lỗi xảy ra trong quá trình đăng nhập.');
        }
    });

});
