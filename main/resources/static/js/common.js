//展示loading
function g_showLoading(){
	var idx = layer.msg('Processing...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: '0px', time:100000}) ;  
	return idx;
}
//salt
var g_passsword_salt="abcdefg1234567"
