webpackJsonp([25],{"3biF":function(e,t){},"4/WD":function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=s("LPk9"),a=s("FJop"),n=s("2tLR"),i=s("YgNb"),r={data:function(){return{fellPathSetInterval:null,encrypted:!1,imageUrl:"",keyStorePath:"",keystoreInfo:""}},components:{Back:o.a,Password:a.a},mounted:function(){},methods:{keystore:function(){var e=this,t=document.getElementById("fileId");t.click(),t.onchange=function(){""!==this.value?"keystore"===t.files[0].name.toLowerCase().split(".").splice(-1)[0]?(e.readFiles(t),setTimeout(function(){if(""!==e.keystoreInfo)if("null"===JSON.parse(e.keystoreInfo).encryptedPrivateKey){var t={accountKeyStoreDto:JSON.parse(e.keystoreInfo),password:"",overwrite:!1};e.postKeyStore(t)}else e.$refs.password.showPassword(!0);else e.$message({type:"warning",message:e.$t("message.c194"),duration:"2000"})},500)):e.$message({type:"warning",message:e.$t("message.c194"),duration:"2000"}):e.$message({type:"warning",message:e.$t("message.c194"),duration:"2000"})}},readFiles:function(e){var t=this;if(window.FileReader){var s=e.files[0],o=(s.name.split(".")[0],new FileReader);o.onload=function(){t.keystoreInfo=this.result},o.readAsText(s)}else if(void 0!==window.ActiveXObject){var a=void 0;(a=new ActiveXObject("Microsoft.XMLDOM")).async=!1,a.load(e.value),t.keystoreInfo=a.xml}else if(document.implementation&&document.implementation.createDocument){var n=void 0;(n=document.implementation.createDocument("","",null)).async=!1,n.load(e.value),t.keystoreInfo=n.xml}else alert("error")},toClose:function(e){e||(document.getElementById("fileId").value="")},toSubmit:function(e){var t={accountKeyStoreDto:JSON.parse(this.keystoreInfo),password:e,overwrite:!0};this.postKeyStore(t)},postKeyStore:function(e){var t=this;this.importAccountLoading=!0,Object(n.i)(e).then(function(e){e.success?(localStorage.setItem("newAccountAddress",e.data.value),localStorage.setItem("addressRemark",""),Object(n.b)(e.data.value).then(function(e){e.success&&localStorage.setItem("addressAlias",e.data.alias)}),localStorage.setItem("encrypted",t.encrypted.toString()),t.getAccountList(),t.$message({type:"success",message:t.$t("message.passWordSuccess")})):t.$message({type:"warning",message:t.$t("message.passWordFailed")+e.data.msg}),t.importAccountLoading=!1}).catch(function(e){t.getAccountList(),t.$message({type:"success",message:t.$t("message.c197"),duration:"3000"}),t.importAccountLoading=!1})},getAccountList:function(){var e=this;Object(i.f)().then(function(t){t.success&&(e.$store.commit("setAddressList",t.list),1===t.list.length?e.$router.push({name:"/wallet"}):e.$router.push({name:"/userInfo"}))})},importKey:function(){this.$router.push({path:"/firstInto/firstInfo/importKey"})}}},c={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"import-account"},[s("Back",{attrs:{backTitle:this.$t("message.firstInfoTitle")}}),e._v(" "),s("h1",[e._v(e._s(e.$t("message.inportAccount")))]),e._v(" "),s("input",{staticClass:"hidden",attrs:{type:"file",id:"fileId"}}),e._v(" "),s("p",{staticClass:"hidden",attrs:{id:"preview",value:""}}),e._v(" "),s("div",{staticClass:"keystore",on:{click:e.keystore}},[s("h1",[e._v(e._s(e.$t("message.c189")))]),e._v(" "),s("p",[e._v(e._s(e.$t("message.c190"))),s("br"),e._v(e._s(e.$t("message.c191")))]),e._v(" "),s("h3",{directives:[{name:"show",rawName:"v-show",value:!1,expression:"false"}]},[e._v("\n      "+e._s(e.$t("message.c192"))+"\n    ")])]),e._v(" "),s("div",{staticClass:"key text-d cursor-p",on:{click:e.importKey}},[e._v(e._s(e.$t("message.c193")))]),e._v(" "),s("Password",{ref:"password",on:{toSubmit:e.toSubmit,toClose:e.toClose}})],1)},staticRenderFns:[]};var l=s("vSla")(r,c,!1,function(e){s("3biF")},null,null);t.default=l.exports}});