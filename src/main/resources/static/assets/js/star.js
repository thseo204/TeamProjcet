//별점 마킹 모듈 프로토타입으로 생성
function Rating(){};
Rating.prototype.rate = 0;
Rating.prototype.setRate = function(newrate){
    //별점 마킹 - 클릭한 별 이하 모든 별 체크 처리
    this.rate = newrate;
    let items = document.querySelectorAll('.rate_radio');
    items.forEach(function(item, idx){
        if(idx < newrate){
            item.checked = true;
        }else{
            item.checked = false;
        }
    });
}
let rating = new Rating();//별점 인스턴스 생성

document.addEventListener('DOMContentLoaded', function(){
    //별점선택 이벤트 리스너
    document.querySelector('.rating').addEventListener('click',function(e){
        let elem = e.target;
        if(elem.classList.contains('rate_radio')){
            rating.setRate(parseInt(elem.value));
        }
    })
});

    //상품평 작성 글자수 초과 체크 이벤트 리스너
    document.querySelector('.review_textarea').addEventListener('keydown',function(){
        //리뷰 400자 초과 안되게 자동 자름
        let review = document.querySelector('.review_textarea');
        let lengthCheckEx = /^.{400,}$/;
        if(lengthCheckEx.test(review.value)){
            //400자 초과 컷
            review.value = review.value.substr(0,400);
        }
    });

    //저장 전송전 필드 체크 이벤트 리스너
    document.querySelector('#save').addEventListener('click', function(e){
        //별점 선택 안했으면 메시지 표시
        if(rating.rate == 0){
            rating.showMessage('rate');
            return false;
        }
        //리뷰 5자 미만이면 메시지 표시
        if(document.querySelector('.review_textarea').value.length < 5){
            rating.showMessage('review');
            return false;
        }
        //폼 서밋
    });

    Rating.prototype.showMessage = function(type){//경고메시지 표시
        switch(type){
            case 'rate':
                //안내메시지 표시
                document.querySelector('.review_rating .warning_msg').style.display = 'block';
                //지정된 시간 후 안내 메시지 감춤
                setTimeout(function(){
                    document.querySelector('.review_rating .warning_msg').style.display = 'none';
                },1000);
                break;
            case 'review':
                //안내메시지 표시
                document.querySelector('.review_contents .warning_msg').style.display = 'block';
                //지정된 시간 후 안내 메시지 감춤
                setTimeout(function(){
                    document.querySelector('.review_contents .warning_msg').style.display = 'none';
                },1000);
                break;
        }
    }


const checkboxes = document.querySelectorAll('input[type="checkbox"][name="starRating"]');
const checkboxValues = [];

checkboxes.forEach((checkbox) => {
  checkbox.addEventListener('change', () => {
    checkboxValues.length = 0; // clear array
    checkboxes.forEach((cb) => {
      if (cb.checked) {
        checkboxValues.push(cb.value);
      }
    });
    document.getElementById('starRating').value = checkboxValues.length;
  });
});