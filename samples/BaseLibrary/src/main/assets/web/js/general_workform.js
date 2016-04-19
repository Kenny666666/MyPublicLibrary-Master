// 页面初始化方法
jQuery(document).ready(init);


// 初始化方法
function init() {
    resetTabindex();
    window.general.getTableDataCmd();//js端向android端发送命令：去获取表格数据吧！
}
/** 数字键盘是否显示*/
var isShowNumberKeyBoard="false";
/** 当前选中的输入框*/
var curFocus=null;
var count;


var tableParent;//父table
var recItemData;//测量指标数据(列)
var recFuncData;//测量位置数据(行)
var recItemDataMap={};//测量指标数据(列)Map
var recFuncDataMap={};//测量位置数据(行)Map
var cellData;//测量项数据(具体元素)
var isGroupColumnVisible=false;//本组列是否显示
var usedFuncId=new Array();//存放测量位置的数组
var recordItemEnumConfig = []; // 配置管理里的配置项
var splitStr="_general_"; //分隔符
var tableId;//父表格id
var workObjectRule;
var isDynamicTable=false;//是否是动态表格
var needAddDeleteButton = false;//添加每行的删除按钮
var remarkRowCount=0; //备注行数量
/** showtext中的输入框符号*/
var showTextInput="[]";
var underLineStr="$underline$";//特殊的继保表单(模板定义)区分的字符串
var deleteDataType = 4;
//第一列列号
var firstColNo = 1;
//第一列列号
var firstRowlNo = 1;


// 打开或关闭数字键操作
function showOrHideNumberKeyboard() {
    if (isShowNumberKeyBoard == "true") {
        isShowNumberKeyBoard = "false";
        jQuery('input[type=text],textarea').each(function() {
            //取消禁止弹出系统键盘
            var readonly = jQuery(this).attr("readonly");
            if(readonly==null){
                jQuery(this).unbind("click");
                if(curFocus!=null){
                    setBorderStyleBlur(curFocus);
                }
            }
        });
    } else {
        isShowNumberKeyBoard = "true";
        var activeElm = document.activeElement;
        var readonly = jQuery(activeElm).attr("readonly");
        if(readonly==null){
            curFocus=document.activeElement;
            window.general.setKeyboardValue(jQuery(curFocus).val());
        }
        count = 1;
        jQuery('input[type=text],textarea').each(function() {
            var readonly = jQuery(this).attr("readonly");
            var type = jQuery(this).attr("type")
            if(type==null||type=="text"){
                if(!(readonly!=null)){
                    jQuery(this).attr("tabindex",count);
                    count++;
                    jQuery(this).blur();
                    //通过让文本框失去焦点以禁止弹出系统键盘
                    jQuery(this).bind("click", function() {
                        jQuery(this).blur();
                        if(curFocus!=null){
                            setBorderStyleBlur(curFocus);
                        }
                        curFocus = this;
                        setBorderStyleFocus(curFocus);
                        window.general.setKeyboardValue(jQuery(this).val());
                    });
                }
            }
        });
        if(jQuery(curFocus).attr("tabindex")==undefined){
            curFocus=null;
        }else{
            setBorderStyleFocus(curFocus);
        }
    }
}


// 将焦点右移
function focusRight(height,flag){
    if(curFocus!=null){
        var index = jQuery(curFocus).attr("tabindex");
        index++;
        if(index<count){
        var obj = jQuery("*[tabindex='" + index + "']");
            jQuery(obj).click();
        }
    }else{
        var obj = jQuery("*[tabindex='1']");
        jQuery(obj).click();
    }
    scrollToFocus(height,flag);
}

// 将焦点左移
function focusLeft(height,flag) {
    var index = jQuery(curFocus).attr("tabindex");
    index--;
    if(index!=0){
        var obj = jQuery("*[tabindex='" + index + "']");
        jQuery(obj).click();
    }
    scrollToFocus(height,flag);
}
//将焦点上移
function focusUp(height,flag){
    if(curFocus!=null){
        var left = findPosX(curFocus);
        $(curFocus).parent().parent().prev().each(function(){
            $(this).find("td").each(function(i){
                var obj = $(this).children("input")[0] || null;
                var offleft = findPosX(obj);
                if (left == offleft) {
                    var readonly = jQuery(obj).attr("readonly");
                    if(readonly==null){
                        jQuery(obj).click();
                        return false;
                    }
                }
            });

        });

    }else{
        var obj = jQuery("*[tabindex='1']");
        jQuery(obj).click();
    }
    scrollToFocus(height,flag);
}
//将焦点下移
function focusDown(height,flag){
    if(curFocus!=null){
        var left = findPosX(curFocus);

        $(curFocus).parent().parent().next().each(function(){

            $(this).find("td").each(function(i){
                var obj = $(this).children("input")[0] || null;
                var offleft = findPosX(obj);
                console.log("left="+left+",offleft="+offleft);
                if (left == offleft) {
                    var readonly = jQuery(obj).attr("readonly");
                    console.log("readonly="+readonly);
                    if(readonly==null){
                        jQuery(obj).click();
                        return false;
                    }
                }
            });

        });
    }else{
        var obj = jQuery("*[tabindex='1']");
        jQuery(obj).click();
    }
    scrollToFocus(height,flag);
}

//获取控件的绝对位置
function findPosX(obj) {
    if(obj==null){
        return 0;
    }
    var curleft = 0;
    if (obj.offsetParent) { //返回父类元素，大多说offsetParent返回body
       while (obj.offsetParent) {//遍历所有父类元素
        curleft += obj.offsetLeft;//当前元素的左边距
        obj = obj.offsetParent;
       }
    } else if (obj.x) curleft += obj.x;
    return curleft;
}

//设置数字键盘的数字
function setNumber(n) {
    curFocus.value= n;
}

//设置焦点组件显示在可视区
function scrollToFocus(height,flag){
    var top = jQuery(curFocus).offset().top;
    var left = jQuery(curFocus).offset().left;
    var h = curFocus.offsetHeight;
    if(top-height-document.body.scrollTop+h>0){
        window.scroll(left, top-h);
    }else if(top<document.body.scrollTop){
        window.scroll(left, top-h);
    }else if(flag){
        window.scroll(left, top-h);
    }
}

/**
 * 重置键盘下标
 * @returns {}
 */
function resetTabindex(){
    if(isShowNumberKeyBoard =="false"){
        return;
    }
    //重新给所有文本框设置tabindex
    var index = 1;
    jQuery('input[type=text],textarea').each(function() {
        jQuery(this).attr("tabindex",index);
        if(jQuery(curFocus).attr("tabindex")!=index){
            setBorderStyleBlur(curFocus);
        }
        index++;
        jQuery(this).blur();
        //通过让文本框失去焦点以禁止弹出系统键盘
        jQuery(this).bind("click", function() {
            jQuery(this).blur();
            if(curFocus!=null){
                setBorderStyleBlur(curFocus);
            }
            curFocus = this;
            setBorderStyleFocus(curFocus);
            window.general.setKeyboardValue(jQuery(this).val());
        });
    });
}

/**
 * 设置边框样式
 * @param {} curFocus
 * @returns {}
 */
function setBorderStyleFocus(curFocus){
    jQuery(curFocus).css("border","1px solid blue").css("-webkit-tap-highlight-color","rgba(0,0,0,0)").css("-webkit-user-modify","read-write-plaintext-only").css("outline","none");

}

/**
 * 设置边框样式
 * @param {} curFocus
 * @returns {}
 */
function setBorderStyleBlur(curFocus){
    jQuery(curFocus).css("border","1px solid #CDCBCB").css("-webkit-tap-highlight-color","rgba(0,0,0,0)").css("-webkit-user-modify","read-write-plaintext-only").css("outline","none");
}

//初始化表格()
function initTableData(tableId){
    //创建表格,并设置属性
    var table=document.createElement('table');
    table.id=tableId;
    table.border="1";
    table.width="100%";
    table.setAttribute("class","table_list_nolock");
    document.body.appendChild(table);
    tableParent=document.getElementById(tableId);
}

//list转换为map
function listToMap(funcArray,itemArray,cellArray){
    //将JSON数组转换为JSON对象
    for(var i=0;i<itemArray.length;i++){
        recItemDataMap[itemArray[i].id] = itemArray[i];
    }
    //将JSON数组转换为JSON对象
    for(var i=0;i<funcArray.length;i++){
        recFuncDataMap[funcArray[i].id] = funcArray[i];
    }
}

//设置列是否分组显示
function setIsGroupColumnVisible(func){
    if(func && func.parentId==null){
        isGroupColumnVisible=false;
    }else{
        isGroupColumnVisible=true;
    }
}

//JSON对象转换为字符串
function jsonToString(jsonObj){
    return JSON.stringify(jsonObj);
}
//字符串转换为JSON对象
function stringToJson(jsonStr){
    return JSON.parse(jsonStr);
}

//拼接作业记录的表格
function splitWorkRecordTable(workObjectRuleStr,tableIdStr,funcArrayStr,itemArrayStr,cellArrayStr){
    //行
    recFuncData=stringToJson(funcArrayStr);
    //列
    recItemData=stringToJson(itemArrayStr);
    //格子(二维数组,第一维是行,第二位是列)
    cellData=stringToJson(cellArrayStr);
    //清除数组中的数据(重置数组)
    usedFuncId.splice(0,usedFuncId.length);
    //未定义测量位置时规则 1-设备动态行 2-手动动态行
    workObjectRule = workObjectRuleStr;

    //初始化表格数据
    if(tableId == null){
        tableId = tableIdStr;
        //处理普通动态行表格的操作(增加添加行按钮)
        operDynamicTable();
        //初始化表格
        initTableData(tableId);
        //获取备注的行数
        remarkRowCount=window.general.getRemarkRowCount();
    }
    //动态的删除table下的所有行
    var rowCount=tableParent.rows.length;
    for(t=0;t<rowCount;t++){
        tableParent.deleteRow(t);
        rowCount--;
        t--;
    }
    //数据转换
    listToMap(recFuncData,recItemData,cellData);
    //格子数组长度(二维数组,第一维是行,第二维是列)
    var rowLength=cellData.length;
    //外层循环(行)
    for (var i =0; i<rowLength; i++) {
        //父table插入一行
        var dataTR = tableParent.insertRow(i);
        var colLength=cellData[i].length;
        //内层循环(列)
        for (var j =0;  j<colLength; j++) {
            var cell = cellData[i][j];//获取到具体的测量项
            var func = recFuncDataMap[cell.functionId];//根据测量位置ID找到测量位置对象(行)
            var item = recItemDataMap[cell.recordId];//根据测量指标ID找到测量指标对象(列)
            var workObjectId = func.workObjectId;
            //显示text的数组(每次循环都需要new一个，否则会引用上一次循环的值)
            var showTextArr = new Array();
            //排除动态新增行里的删除列
            if(item!=undefined){
                //showtext 是用来显示一个测量项有多个输入框的(模板定义)
                var showTxt = item.showText;
                //测量记录的模板定义(列)
                if(showTxt!=undefined && showTxt!=null &&  showTxt.length>0){
                    showTextArr=showTxt.split(showTextInput);
                }
                showTxt = cell.showText;
                //测量项的模板定义(具体的测量项，优先级比列的要高!!)
                if(showTxt!=undefined &&  showTxt!=null &&  showTxt.length>0){
                    showTextArr=showTxt.split(showTextInput);
                }
            }
            var isRowspan = false;//是否合并行了
            //判断测量位置是否还没有被拼接过
            if (func && jQuery.inArray(func.id,usedFuncId) == -1) {
                setIsGroupColumnVisible(func);
                //列是否是分组显示
                if (isGroupColumnVisible) {
                    //根据parentId获取parent的测量位置对象
                    var parentFunc = recFuncDataMap[func.parentId];//父测量位置对象
                    if (parentFunc && jQuery.inArray(parentFunc.id, usedFuncId) == -1) {
                        var dataTD = dataTR.insertCell(j);//行插入列
                        var pFunName=parentFunc.functionName;
                        if(typeof(pFunName)=="undefined"){
                            pFunName="";
                        }
                        dataTD.setAttribute("rowspan", parentFunc.rowspanNum);//合并行
                        dataTD.setAttribute("style", "text-align: center; vertical-align: middle;");//设置样式
                        dataTD.innerHTML = "<span title='"+parentFunc.id+"'>" + pFunName + "</span>";
                        isRowspan=true;
                        usedFuncId.push(parentFunc.id);
                    }
                }
                var dataTD = dataTR.insertCell(isRowspan?(j+1): j);
                dataTD.setAttribute("style", "text-align: center; vertical-align: middle;");
                if (!isGroupColumnVisible) {
                    dataTD.setAttribute("colspan", 2);
                }
                var funcName=func.functionName;
                if(typeof(funcName) == "undefined"){
                    funcName="";
                }
                dataTD.innerHTML = "<span title='"+func.id+"'>" + funcName + "</span>";
                usedFuncId.push(func.id);
            }
            //获取当前行下面的TD个数
            var tdNum=dataTR.childNodes.length;
            var cellTD = dataTR.insertCell(tdNum);
            cellTD.setAttribute("style", "text-align: center; vertical-align: middle;");
            //如果测量位置是备注,则需要合并列
            if(func.isRemark==2){
                cellTD.setAttribute("colspan",recItemData.length);
                cellTD.setAttribute("sort","A"+cell.sort);
                cell.isRemark=2;
            }else{
                cell.isRemark=1;
            }

            var dataType;
            //如果没有数据类型则默认赋一个文本框1
            if(item==undefined){
                dataType=1;
            }else{
                dataType=item.dataType;//获取具体的测量项类型
            }

            //定义格子的名称
            var cellName=cell.functionId+splitStr+cell.recordId+splitStr+cell.isRemark;
            var txtValue=cell.resultValue;
            if (typeof(txtValue) == "undefined") {
                txtValue="";
            }
            //文本
            if(dataType==1){
                var html="<span> ";
                if(showTextArr!=null && showTextArr.length>0){
                    var txtArray=txtValue.split(";");
                    var txtIndex=0;
                    for(var p=0;p<showTextArr.length;p++){
                        //这种特殊的表单需多加一个分隔符
                        cellName+=splitStr+underLineStr;
                        if(showTextArr[p].indexOf(splitStr)>-1){
                            html+=showTextArr[p].replace(new RegExp(splitStr,"gm"),"");
                        }else{
                            html+=showTextArr[p];
                        }
                        if(showTextArr[p].indexOf(splitStr)<=0 && p!=showTextArr.length-1){
                            var cellValue = "";
                            if(txtArray[txtIndex] != undefined && txtArray[txtIndex] != "undefined"){
                                cellValue = txtArray[txtIndex];
                            }
                            html+="<input workObjectId='"+ workObjectId+"' type='text' style='margin-left:2px;margin-right:2px;text-align:center; border:0px;border-bottom:red 1px solid;width:35%;line-height:22px;' name='"+cellName+"' value='"+cellValue+"'"+"/>";
                            txtIndex++;
                        }
                    }
                    html+="</span>";
                    cellTD.innerHTML=html;
                }else{
                    html+=" <input workObjectId='"+ workObjectId+"' type='text'";
                    var minValue;
                    var maxValue;
                    //非备注的格子才有最大值最小值
                    if(item!=undefined && cell.isRemark ==1){
                        minValue=item.minValue;//最小值
                        maxValue=item.maxValue;//最大值
                        html+=" onblur='checkNumRange(this," + minValue + ","+ maxValue + ")'";
                    }
                    html+=" name='"+cellName+"' value='"+txtValue+"' /></span>";
                    cellTD.innerHTML = html;
                }
            //数字
            }else if(dataType==2){
                var html="<span> ";
                    if(showTextArr!=null && showTextArr.length>0){
                        var txtArray=txtValue.split(";");
                        var txtIndex=0;
                        for(var p=0;p<showTextArr.length;p++){
                            //这种特殊的表单需多加一个分隔符
                            cellName+=splitStr+underLineStr;
                            if(showTextArr[p].indexOf(splitStr)>-1){
                                html+=showTextArr[p].replace(new RegExp(splitStr,"gm"),"");
                            }else{
                                html+=showTextArr[p];
                            }
                            if(showTextArr[p].indexOf(splitStr)<=0 && p!=showTextArr.length-1){
                                var cellValue = "";
                                if(txtArray[txtIndex] != undefined && txtArray[txtIndex] != "undefined"){
                                    cellValue = txtArray[txtIndex];
                                }
                                html+="<input workObjectId='"+ workObjectId+"' type='text'";
                                html+=" style='margin-left:2px;margin-right:2px;text-align:center; border:0px;border-bottom:red 1px solid;width:35%;line-height:22px;' name='"+cell.functionId+splitStr+cell.recordId+splitStr+cell.isRemark+"' value='"+cellValue+"'"+"/>";
                                txtIndex++;
                            }
                        }
                        html+="</span>";
                        cellTD.innerHTML=html;
                    }else{
                        html+=" <input workObjectId='"+ workObjectId+"' type='text'";
                        //html+=" onblur='checkNumRange(this," + minValue + ","+ maxValue + ")'";
                        html+=" name='"+cell.functionId+splitStr+cell.recordId+splitStr+cell.isRemark+"' value='"+txtValue+"' /></span>";
                        cellTD.innerHTML=html;
                    }
            //枚举(单选或多选)
            }else if(dataType==3){
                var html="<span> ";
                if(showTextArr!=null && showTextArr.length>0){
                    var txtArray = txtValue.split(";");
                    var txtIndex=0;
                    for(var p=0;p<showTextArr.length;p++){
                        //这种特殊的表单需多加一个分隔符
                        cellName+=splitStr+underLineStr;
                        if(showTextArr[p].indexOf(splitStr)>-1){
                            html+=showTextArr[p].replace(new RegExp(splitStr,"gm"),"");
                        }else{
                            html+=showTextArr[p];
                        }
                        if(showTextArr[p].indexOf(splitStr)<=0 && p!=showTextArr.length-1){
                            var cellValue = "";
                            if(txtArray[txtIndex] != undefined && txtArray[txtIndex] != "undefined"){
                                cellValue = txtArray[txtIndex];
                            }
                            html+="<input workObjectId='"+ workObjectId+"' type='text' style='margin-left:2px;margin-right:2px;text-align:center; border:0px;border-bottom:red 1px solid;width:35%;line-height:22px;' name='"+cellName+"' value='"+cellValue+"'"+"/>";
                            txtIndex++;
                        }
                    }
                    html+="</span>";
                    cellTD.innerHTML=html;
                }else{
                    //通过测量指标ID获取配置信息
                    var enumConfigItem= stringToJson(window.general.getConfigByItemId(item.id));
                    //多选
                    if(item.selectMode == "Multi"){
                        if (enumConfigItem && enumConfigItem.length > 0) {
                            var ckIdArray=new Array();
                            for (var k = 0; k < enumConfigItem.length; k++) {
                                var code=enumConfigItem[k].code;
                                var name=enumConfigItem[k].name;
                                //随机生成一个id
                                var ckId=window.general.getUUID();
                                ckIdArray.push(ckId);
                                html+="<input workObjectId='"+ workObjectId+"' type='checkbox' id='"+ckId+"' name='"+cell.functionId+splitStr+cell.recordId+splitStr+cell.isRemark+"' value='"+code+"'>"+name+"</input>";
                            }
                            html+="</span>";
                            cellTD.innerHTML=html;
                            for (var k = 0; k < enumConfigItem.length; k++) {
                                var code=enumConfigItem[k].code;
                                var ckId=ckIdArray[k];
                                //如果传进来的value包含code，则让其选中
                                if(txtValue != undefined && txtValue.indexOf(code)>-1){
                                    document.getElementById(ckId).checked=true;
                                }
                            }
                        }
                    //单选
                    }else{
                        //随机生成一个id
                        var selectId=window.general.getUUID();
                        html = "<select workObjectId='"+ workObjectId+"' id="+selectId+" name='"+cell.functionId+splitStr+cell.recordId+splitStr+cell.isRemark+"'>";
                        if (enumConfigItem && enumConfigItem.length > 0) {
                            //默认选择的下标
                            var selecedIndex=0;
                            for (var k = 0; k < enumConfigItem.length; k++) {
                                var code=enumConfigItem[k].code;
                                var name=enumConfigItem[k].name;
                                //如果传进来的value等于code，则赋值选中下标
                                if(txtValue==code){
                                    selecedIndex=k;
                                }
                                html+="<option value='"+name+"'>"+name+"</option><br/>";
                            }
                            html+="</select>";
                            html+="</span>";
                            cellTD.innerHTML=html;
                            //设置单选框的默认值
                            document.getElementById(selectId).options[selecedIndex].selected=true;
                        }
                    }
                }
            //按钮(删除)
            }else if(dataType==deleteDataType){
                cellTD.innerHTML="<span> <input type='button' onclick='deleteRow("+i+")' value='删除' /> </span>";
            }
            //如果有公式，则不让用户手动填写,加一个默认的样式区分
            if(undefined!=cell.formula){
                //$("input[name='"+cellName + "']").attr("readonly","readonly");
                //$("input[name='"+cellName + "']").attr("style","border:red 1px solid");
            }
        }
    }
    // 表头
    var header = tableParent.createTHead();
    header.setAttribute("class", "grid-head-table");
    var headRow = header.insertRow();
    if (recFuncData && recFuncData.length > 0) {
        var firstHeadCell = document.createElement("th");
        firstHeadCell.setAttribute("class", "text-center");
        firstHeadCell.setAttribute("colspan", 2);
        //firstHeadCell.innerHTML = "我是题头~~~";
        headRow.appendChild(firstHeadCell);
    }

    jQuery.each(recItemData, function(index, data){
        var headCell = document.createElement("th");
        headCell.setAttribute("class", "text-center");
        headCell.innerHTML = "<span title='"+data.recordName+"'>" + data.recordName + "</span>";
        headRow.appendChild(headCell);
    });

    //为动态新增行添加删除的按钮
    if(isDynamicTable && needAddDeleteButton){
        addDeleteButton();
    }
}


/**
 * 计算有公式的表格
 * @returns {}
 */
function calcFormulaCell(){
    var cellLength=cellData.length;
    //外层循环(行)
    for (var i =0; i<cellLength; i++) {
        var rowLength=cellData[i].length;
        //内层循环(列)
        for (var j =0;  j<rowLength; j++) {
            var cell = cellData[i][j];//获取到具体的测量项
            var cellName=cell.functionId+splitStr+cell.recordId+splitStr+cell.isRemark;
            var formula=cell.formula;

            //如果有公式
            if(undefined!=formula){
                //先将计算公式转换为json格式
                var ret=window.general.parseFormula2Json(formula);
                var jsonFormula=stringToJson(ret);

                //遍历json，并给相应的key赋值
                for(var key in jsonFormula){
                   if(key.indexOf("C")>-1){
                       //有R和C的是普通行
                       var rowNo=parseInt(key.substring(1, key.indexOf("C")),10) ;
                       var colNo=parseInt(key.substring(key.indexOf("C")+1),10);
                       var value=$("table").find("tr").eq(rowNo).children("td").eq(colNo).find("input").val();
                       jsonFormula[key]=value;
                   }else if(key.indexOf("A")>-1){
                       //有A的是附加行
                       var value=$("table").find("tr td[sort='"+key+"']").find("input").val();
                   }
                   //将公式里的字符替换成具体的值(相当于replaceAll)
                   formula=formula.replace(new RegExp(key,"gm"),value);
                }

                //调用java代码，计算具体的公式
                var calcRet=window.general.calcFormula(cell.rowNo,cell.colNo,formula);
                //将计算出来的结果显示到格子上,正确的变蓝，错误的变红
                if(undefined!=calcRet){
                    $("input[name='"+cellName + "']").attr("value",calcRet);
                    $("input[name='"+cellName + "']").attr("style","border:blue 2px solid");
                }else{
                    window.general.showToast("计算公式出错,详情查日志文件");
                    $("input[name='"+cellName + "']").attr("value",null);
                    $("input[name='"+cellName + "']").attr("style","border:red 2px solid");
                }
            }
        }
    }
}

/*
* 保存作业记录数据
*/
function saveWorkRecordData(isReturnBack){
    //定义一个对象(Json)
    var    tempData=[];
    //循环遍历table下面的所有输入框(包括单选和多选框)
    $("table tr td:has(:input)").each(function(){
        //checkbox的值(因为有多选，所以需要拼接)
        var ckValue="";
        //input的值(模板定义类型需要拼接)
        var inputValue= "";
        $(this).find(":input").each(function(){
             var workObjectId = $(this).attr("workObjectId");
             var arr = this.name.split(splitStr);
            //输入框
            if(this.type=="text"){
                //如果是特殊的表单就需要加分号隔开，否则不需要
                if(this.name.indexOf(underLineStr)>-1){
                    inputValue+=(this.value+";");
                }else{
                    inputValue=this.value;
                }
                if(arr!=null && arr.length>=2 ){
                    //给json对象赋值
                    var obj={value:inputValue.substring(0,inputValue.length),functionId:arr[0],recordId:arr[1],isRemark:arr[2],workObjectId:workObjectId};
                    //把json对象放入json数组
                    tempData.push(obj);
                }
            //复选框
            }else if(this.type=="checkbox"){
                //无论是选中还是未选中，都要在后面拼接分号
                ckValue+=(this.checked?this.value+";":""+";");
                var obj={value:ckValue.substring(0,ckValue.length),functionId:arr[0],recordId:arr[1],isRemark:arr[2],workObjectId:workObjectId};
                tempData.push(obj);
            //单选框
            }else if(this.type=="select-one"){
                var obj={value:window.general.getConfigCodeByItemId(arr[1],this.value),functionId:arr[0],recordId:arr[1],isRemark:arr[2],workObjectId:workObjectId};
                tempData.push(obj);
            }
        });
    });
    //将Json对象转换为字符串
    var jsonString=JSON.stringify(tempData);
    //将网页中的值传到android客户端
    window.general.saveWorkRecordDataCmd(jsonString,isAddRow,isReturnBack);
}

/**
 * 处理普通动态行表格的操作(增加添加行按钮)
 */
function operDynamicTable(){
    //如果测量位置为空或者有已添加的动态新增行，则是动态表格
    if((recFuncData == null || recFuncData.length==0)  || (null!=recFuncData && recFuncData[0].isDynamic==1)){
        if(workObjectRule != 1){
            var addRowDiv = jQuery("<div style='width:100%;text-align:right;'><input type='button' id='btn_add_row' value='添加行' style='width:15%; text-align:center; padding:7px 8px 7px 8px;' /></div>");
            jQuery("body").append(addRowDiv);
            var btnAddRow=document.getElementById('btn_add_row');
            btnAddRow.onclick= function(){
                addRow("del_id");
            };
            //增加删除列
            var delJson={id: "del_id",recordName: "删除",dataType: deleteDataType};
            recItemData.push(delJson);
            isDynamicTable=true;
            needAddDeleteButton=true;
        }
    }
}

/**
 * 添加删除的按钮(下载下来的动态新增行)
 * @returns {}
 */
function addDeleteButton(){
    //获取table当前行数
    var rowCount = tableParent.rows.length;
    var dynamicJson={};
    for (var i =0; i<recFuncData.length; i++) {
        //如果是备注的行，则不添加按钮
        if("2" == recFuncData[i].isRemark){
            continue;
        }
        //TODO 需要吗?
        var uuid=window.general.getUUID();
        var cellJson={id:uuid ,recordId:"del_id", functionId:recFuncData[i].id, rowNo :i+firstRowlNo, colNo:recItemData.length+firstColNo};
        cellData[i].push(cellJson);
//        dynamicJson.id=uuid;
//        dynamicJson.record_id="del_id";
//        dynamicJson.function_id=recFuncData[i].id;
//        dynamicJson.row_no=i+1;
//        dynamicJson.col_no=recItemData.length+1;
//        dynamicJson.sort=999999999; //9个9表示很大很大...
        //插入动态新增行的数据
        //window.general.addDynamicRow(jsonToString(dynamicJson));
    }
    //添加完成之后设置一个标识，下一次不用再添加
    needAddDeleteButton = false;
    //重新排序
    sortToCell();
    //再次拼接表格
    splitWorkRecordTable(workObjectRule,tableId,jsonToString(recFuncData),jsonToString(recItemData),jsonToString(cellData));
}


 /**
  * 动态新增行
  * @param {} recordId 测量指标的id
  * @param {} deleteId 删除按钮的id
  * @returns {}
  */
function addRow(deleteId){
    //获取table当前行数
    var rowCount=tableParent.rows.length;
    if(rowCount > 1){
        isAddRow = true;
        //新增行之前先保存之前的数据
        saveWorkRecordData();
        //因为有新增记录了,行信息和格子信息需要从数据库中获取了,因为这里需要实时的获取最新的行信息和格子信息
        //所以这里的保存数据方法不能使用异步
        //获取当前的行信息
        //recFuncData = stringToJson(window.general.getFuncArrayStr());
        //获取当前的格子信息
        //cellData = stringToJson(window.general.getCellArrayStr());
    }
    //生成的uuid作为测量位置id(function id)
    var funcId = window.general.getUUID();
    //增加测量项
    var cellJson={};
    var cellJsonArray=[];
    var dynamicJson={};
    //新的行号
    var newRowNo = rowCount-remarkRowCount;
    // 增加测量位置(行)
    var funcJson={id: funcId,
      parentId: null,
      functionName:newRowNo,
      isRemark: 1};
    recFuncData.push(funcJson);


    //测量项的子项的长度为测量指标的长度
    for(var i=0;i<recItemData.length;i++){
        var uuid = window.general.getUUID();
        cellJson={id:uuid,recordId: (i+1==recItemData.length) ? deleteId:recItemData[i].id, functionId:funcId, rowNo :newRowNo, colNo:firstColNo+i}
        cellJsonArray.push(cellJson);
        dynamicJson.id = uuid;
        dynamicJson.function_id = funcId;
        dynamicJson.record_id = (i+1==recItemData.length) ? deleteId:recItemData[i].id;
        dynamicJson.row_no = newRowNo;
        dynamicJson.col_no = firstColNo + i;
        //sort计算公式= (当前行号-1) * (列数-1) + (i+1)
        //列数-1是因为前面手动给列数增加了一个删除的列
        //i+1 是因为i是从0开始不是1开始
        var sort = (newRowNo-1) * (recItemData.length-1) + (i+1) ;
        //最后一个格子是放删除按钮,所以它的sort特殊处理一下
        dynamicJson.sort = (i+1==recItemData.length) ? 999999999: sort;
        //最后一个格子是删除按钮,不保存到数据库
        if((i+1) != recItemData.length){
            //插入动态新增行的数据
            window.general.addDynamicRow(jsonToString(dynamicJson));
        }
    }
    cellData.push(cellJsonArray);
    //重新排序
    sortToCell();
    //再次拼接表格
    splitWorkRecordTable(workObjectRule,tableId,jsonToString(recFuncData),jsonToString(recItemData),jsonToString(cellData));

    isAddRow=false;
}
/** 是否是新增行*/
var isAddRow=false;

/**
 * 对测量项重新进行排序,备注永远在最后一行!
 * @returns {}
 */
function sortToCell(){
    var tempCellData=[];
    for(var i=0;i<cellData.length;i++){
        var jsonArray=cellData[i];
        for(var j=0;j<jsonArray.length;j++){
            if(2!=jsonArray[j].isRemark){
                tempCellData.push(jsonArray);
                break;
            }
        }
    }
    for(var i=0;i<cellData.length;i++){
        var jsonArray=cellData[i];
        for(var j=0;j<jsonArray.length;j++){
            if(2==jsonArray[j].isRemark){
                tempCellData.push(jsonArray);
                break;
            }
        }
    }
    cellData=tempCellData;
}


/**
 * 动态删除行
 * rowNum :数组的下标(非tr的下标)
 */
function deleteRow(rowNum){
    var rowCount=tableParent.rows.length;
    if(rowNum< rowCount-remarkRowCount-2){
        window.general.showToast("请从最后一行开始删除");
        return;
    }

    //先删除数据库中的数据(参数是行号，并非下标)
    window.general.deleteDynamicRow(cellData[rowNum][0].rowNo);

    if(rowCount>1){

        cellData.splice(rowNum,1);
        recFuncData.splice(rowNum,1);
        tableParent.deleteRow(rowNum+1);

        isAddRow=true;
        saveWorkRecordData();
    }
    //再重新拼接表格
    //splitWorkRecordTable(workObjectRule,tableId,jsonToString(recFuncData),jsonToString(recItemData),jsonToString(cellData));
    isAddRow=false;
}

//检查数字的最大值最小值是否符合要求
function checkNumRange(inputObj,minValue,maxValue){
    var value = $(inputObj).val();
    if(value==null || value =="" || value ==undefined){
        return;
    }
    var floatValue = parseFloat(value);
    if(!isNaN(floatValue)){
        //当前值比最小值小或者比最大值大,显示红色框
        if((!isNaN(minValue) && value < minValue) || (!isNaN(maxValue) && value > maxValue)){
            $(inputObj).attr("style","border:red 2px solid");
        }else{
        }
    }else{
        $(inputObj).attr("style","border:red 2px solid");
    }
}

