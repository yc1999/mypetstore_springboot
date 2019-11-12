<!--变换验证码图片-->
function changeCodeImage() {
    document.getElementById("checkImg").click()
    {
        this.src = this.src + '?' + Math.random();
    }
}