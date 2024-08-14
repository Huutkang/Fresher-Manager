document.addEventListener('DOMContentLoaded', () => {
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
    
    // hiện ẩn mật khẩu
    togglePassword.addEventListener('click', () => {
        const type = passwordInput.type === 'password' ? 'text' : 'password';
        passwordInput.type = type;
        if (togglePassword.src.includes('eye-icon.png')) {
            togglePassword.src = 'eye-slash-icon.png';
        } else {
            togglePassword.src = 'eye-icon.png';
        }
    });
    
    // Xử lý việc gửi biểu mẫu
    document.getElementById('loginForm').addEventListener('submit', (event) => {
        event.preventDefault();
        
        // Lưu thông tin người dùng vào bộ nhớ cục bộ
        const username = usernameInput.value;
        localStorage.setItem('username', username);
        
        // Hiển thị cửa sổ bật lên
        alert(`Xin chào: ${username}`);
        
    });
});
