
const characters ='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
const BASE_URL = window.location.origin+"/auth"
var draftClient;

onLoad = () =>{
    const url = BASE_URL+"/create/initiateClientCreation";
    draftClient = JSON.parse(call("GET",url));
}
function submitClient(){
    var clientId = document.getElementById("clientId").value;
    var clientSecret = document.getElementById("clientSecret").value;
    var clientName = document.getElementById("appName").value;
    var redirectURI = document.getElementById("redirectUrl").value;
    draftClient.isOnboarded=true;
    let RSAEncrypt = new JSEncrypt();
    RSAEncrypt.setPublicKey(draftClient.publicKey);
    const encryptedClientId = RSAEncrypt.encrypt(clientId);
    const encryptedClientSecret = RSAEncrypt.encrypt(clientSecret);
    draftClient.clientId = btoa(encryptedClientId);
    draftClient.clientSecret = btoa(encryptedClientSecret);
    draftClient.clientName = clientName;
    draftClient.redirectUri = redirectURI;

    const url = BASE_URL+"/create/client";
    call("POST",url,draftClient);
}
submit = () =>{
    console.log("submit works")
}

generateClientCredentials = () =>{
    var editDiv = document.getElementById("editDiv");

    var label1 = document.createElement("label");
    var label2 = document.createElement("label");
    var clientId = document.createElement("input");
    var clientSecret = document.createElement("input");

    clientId.setAttribute("type","text");
    clientId.setAttribute("id","clientId");
    clientId.disabled = true;
    clientId.setAttribute("value",generateString(16))
    label1.innerText = "Client Id";
    editDiv.appendChild(label1);
    editDiv.appendChild(clientId);


    clientSecret.setAttribute("type","password");
    clientSecret.setAttribute("id","clientSecret");
    clientSecret.disabled=true;
    clientSecret.setAttribute("value",generateString(16));
    label2.innerText = "Client Secret";
    editDiv.appendChild(label2);
    editDiv.appendChild(clientSecret);

    var eye = document.createElement("i");
    eye.setAttribute("aria-hidden","true");
    eye.setAttribute("class","fa fa-eye");
    editDiv.appendChild(eye);
    editDiv.appendChild(document.createElement("hr"));
    editDiv.appendChild(document.createElement("br"));
    
    var generateBtn = document.getElementById("generate");
    generateBtn.parentNode.removeChild(generateBtn);

    var submitButton = document.createElement("button");
    submitButton.innerText="Create Client"
    submitButton.setAttribute("type","button");
    submitButton.setAttribute("onclick","submitClient()");
    var clientForm = document.getElementById("clientForm");
    clientForm.appendChild(submitButton);
}

function generateString(length) {
    let result = '';
    const charactersLength = characters.length;
    for ( let i = 0; i < length; i++ ) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}
call = (callType, url,body=null, callBack = ()=>{})=>{
    let xmlHttpReq = new XMLHttpRequest();
    xmlHttpReq.open(callType, url, false);
    var payload;
    if(body != null){
        xmlHttpReq.setRequestHeader('Content-Type', 'application/json')
        payload = JSON.stringify(body);
    }
    xmlHttpReq.send(payload);
    callBack();
    return xmlHttpReq.responseText;
}
onLoad();

