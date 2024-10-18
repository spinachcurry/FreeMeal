document.addEventListener("DOMContentLoaded", function() { 
    const modal1 = document.getElementById("modal1"); 
    const modal2 = document.getElementById("modal2");
    const cuisinesBtn = document.getElementById("CuisinesBtn");
    const FilterBtn = document.getElementById("FilterBtn");
    const closeBtn = document.getElementById("closeBtn");

   
   // Cuisines 버튼 클릭 시 모달 열기
    cuisinesBtn.addEventListener("click", function() {
        modal1.style.display = "block";
        CuisinesForm();// CuisinesForm 폼을 기본으로 보이게 설정
    }); 
 // Filter 버튼 클릭 시 모달 열기
    FilterBtn.addEventListener("click", function() {
        modal2.style.display = "block";
        Filterform();// FilterBtn 폼을 기본으로 보이게 설정
    });	
    // 닫기 버튼 클릭 시 모달 닫기
    closeBtn.addEventListener("click", function() { 
        modal1.style.display = "none";
       
    }); 
     window.addEventListener("click", function(event) {
        // 클릭한 요소가 modal 또는 modal1인 경우 닫기 
         if (event.target === modal1) {
            modal1.style.display = "none";
        }
        if (event.target === modal2) {
            modal2.style.display = "none";
        }
        
    });
});
  
// 필터 폼 보여주기;
function Filterform() {
    const modalBody = document.getElementById("modal-body");
    if (modalBody) {
        modalBody.innerHTML = `
        <h2>필터</h2>
         <form id="Filterform">  
			<d	iv class="row">
			 <div class="col-md-4">.col-md-4</div>
			 <div class="col-md-4 ms-auto">.col-md-4 .ms-auto</div>
			</div>
			<div class="row">
			  <div class="col-md-3 ms-auto">.col-md-3 .ms-auto</div>
			  <div class="col-md-2 ms-auto">.col-md-2 .ms-auto</div>
			</div> 
		 </form>
        `;
    }
}
 