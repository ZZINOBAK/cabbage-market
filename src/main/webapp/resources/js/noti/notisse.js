// SSE
const sse = new EventSource("/connect");
//커넥트 = 구독하고 있는 sseemitter 객체

sse.addEventListener('sseServer', e => {  
    const receivedData = e.data;
    console.log("이것은 서버에서 보내준 변경된 데이터다.",receivedData);  
    
 	if (receivedData != null) {
 	    	const dataObject = JSON.parse(receivedData);
 	
            const url = dataObject.url;
            const notiType = dataObject.notiType;
			const fileName = dataObject.fileName;
			const notiContent = dataObject.notiContent;
			const notiId = dataObject.notiId;

            const someElement = document.getElementById('someElement');
		
			if (someElement) {
				const alrim = document.getElementById('alrim');
            	if (alrim.style.display === 'none') {
            	
				    let currentNumber = parseInt(someElement.innerText.trim(), 10) || 0;
				    currentNumber++;
				    someElement.innerHTML = 
				        `<span class="alrim alrimCount" id="alrimCount" style="display: block;">
				            ${currentNumber}
				        </span>`;
			    }
			    //알림창이 열려있다면 바로 읽음처리
			    if (alrim.style.display === 'block') {
			    	$.ajax({
						type: "get",
						url : "updateNoti",
						success:function(data){
				        	console.log("notisse.js에서 읽음으로 변경");
						}
					});
			    }
			}
            
            
            
            const alrimList = document.getElementById('alrimList');
			const newLi = document.createElement('li');
			newLi.className = 'none-haeun-li';
			newLi.id = `alrim_${notiId}`;
			console.log(notiType);
			
			// 현재 #goNotiDelete의 상태 확인
    		let isHidden = $("#goNotiDelete").css("display") === "none";
    
			if (notiType === '키워드' || notiType === '가격 변동') {
				console.log("sse 키워드, 가격변");
			
			    newLi.innerHTML = `<a class="none-haeun-a" href="${url}" style="font-size: 18px;">
			        <div class="notification">
			            <div class="notiInfo notiContainer">
			                <div class="notiPicContainer">
			                    <img class="notiPic" src="/resources/pic/postPic/${fileName}" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'" alt="알림 이미지">
			                </div>
			                <div class="titleContainer">
			                    <span class="title-small"> [${notiType} 알림] </span>
			                    <div class="notiContentContainer">
			                        <span class="notiContent-small"> ${notiContent} </span>
			                    </div>
			                </div>
			            </div>
			        </div>
			    </a>
    			<button onclick="goNotiDelete('${notiId}')" id="goNotiDelete" style="display:${isHidden ? 'none' : 'block'};">
			    X
 				</button>`;
 			} else if (notiType === '문의글 답변') {
				console.log("sse 문의글 답");
			    newLi.innerHTML = `<a class="none-haeun-a" href="${url}" style="font-size: 18px;">
			        <div class="notification">
			            <div class="notiInfo notiContainer">
			                <div class="notiPicContainer">
			                    <img class="notiPic" src="/resources/pic/img/munigeul_icon.png" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'" alt="알림 이미지">
			                </div>
			                <div class="titleContainer">
			                    <span class="title-small"> [${notiType} 알림] </span>
			                    <div class="notiContentContainer">
			                        <span class="notiContent-small"> ${notiContent} </span>
			                    </div>
			                </div>
			            </div>
			        </div>
			    </a>
			    <button onclick="goNotiDelete('${notiId}')" id="goNotiDelete" style="display:none;">
			    X
 				</button>`;
 			} else if (notiType === '온도 변동') {
				console.log("sse 온도 변동");
			    newLi.innerHTML = `<a class="none-haeun-a" href="${url}" style="font-size: 18px;">
			        <div class="notification">
			            <div class="notiInfo notiContainer">
			                <div class="notiPicContainer">
			                    <img class="notiPic" src="/resources/pic/img/ondo_icon.jpg" onerror="this.onerror=null; this.src='/resources/pic/img/cabbage_icon.png'" alt="알림 이미지">
			                </div>
			                <div class="titleContainer">
			                    <span class="title-small"> [${notiType} 알림] </span>
			                    <div class="notiContentContainer">
			                        <span class="notiContent-small"> ${notiContent} </span>
			                    </div>
			                </div>
			            </div>
			        </div>
			    </a>
			    <button onclick="goNotiDelete('${notiId}')" id="goNotiDelete" style="display:none;">
			    X
 				</button>`;
 			}
						 		
				 			
				 		
									    
			// firstli 클래스명을 가진 요소를 찾습니다.
			const firstLiElement = document.querySelector('.firstLi');

			// firstLiElement 다음에 새로운 li 요소를 추가합니다.
			if (firstLiElement && firstLiElement.parentNode) {
			    firstLiElement.parentNode.insertBefore(newLi, firstLiElement.nextSibling);
			}		    
		    //alrimList.scrollTop = alrimList.scrollHeight;

            if (alrimList.childElementCount > 10) {
                alrimList.removeChild(alrimList.lastChild);
            }
            
     
    }
	
});

    
    
    