const dataTransfer = new DataTransfer();
const uploadFilesArray = [];

$(() => {
  inputOnPriceEventOn();
});

function previewImages(event) {
  var input = event.target;
  var previewContainer = document.getElementById("preview-container");

  // 확장자 제한
  var allowExtension = ["jpg", "jpeg", "png"];

  if (input.files && input.files.length > 0) {
    for (var i = 0; i < input.files.length; i++) {
      const fileName = input.files[i].name;
      const fileExtension = fileName.split(".").pop().toLowerCase();

      // 확장자 검사
      if (allowExtension.indexOf(fileExtension) === -1) {
        alert("jpg, jpeg, png 형식의 이미지만 업로드 가능합니다.");
        return;
      }

      uploadFilesArray.push(input.files[i]);

      let uploadedFiles = input.files;

      var reader = new FileReader();
      reader.onload = function (e) {
        var previewItem = document.createElement("li");
        previewItem.className = "preview-item";

        var previewImage = document.createElement("img");
        previewImage.className = "preview-image";
        previewImage.src = e.target.result;

        var deleteIcon = document.createElement("span");
        deleteIcon.className = "delete-icon";
        deleteIcon.innerHTML = "X";
        deleteIcon.addEventListener("click", function () {
          removeFileFromArray(this);
          previewItem.remove();
        });

        previewItem.appendChild(previewImage);
        previewItem.appendChild(deleteIcon);
        previewContainer.appendChild(previewItem);
      };
      reader.readAsDataURL(input.files[i]);
    }
  }
  // 업로드된 파일 배열을 dataTransfer로 처리
  dataTransfer.items.clear();
  uploadFilesArray.forEach((file) => {
    dataTransfer.items.add(file);
  });

  // 파일 입력 요소에 업로드된 파일 목록 할당
  $("#uploadFile")[0].files = dataTransfer.files;
}

function removeFileFromArray(obj) {
  let div = $(obj).closest(".preview-item");

  let index = div.index();

  uploadFilesArray.splice(index, 1);

  dataTransfer.items.clear();
  //fileArray.forEach(file => { dataTransfer.items.add(file); });
  uploadFilesArray.forEach((file) => {
    dataTransfer.items.add(file);
  });

  //남은 배열을 dataTransfer로 처리(Array -> FileList)
  $("#uploadFile")[0].files = dataTransfer.files; //제거 처리된 FileList를 돌려줌
}

//공백 판독
document.addEventListener("DOMContentLoaded", function () {
  var postTitleInput = document.getElementById("postTitle");
  var postTitleError = document.getElementById("postTitleError");
  var postContentInput = document.getElementById("postContent");
  var postContentError = document.getElementById("postContentError");

  postTitleInput.addEventListener("input", function () {
    var trimmedValue = postTitleInput.value.trim();

    if (trimmedValue === "") {
      postTitleError.style.display = "block";
    } else {
      postTitleError.style.display = "none";
    }
  });
  postContentInput.addEventListener("input", function () {
    var trimmedValue = postContentInput.value.trim();

    if (trimmedValue === "") {
      postContentError.style.display = "block";
    } else {
      postContentError.style.display = "none";
    }
  });
});

function openFileInput() {
  document.getElementById("uploadFile").click();
}
function disableSubmitButton() {
  document.getElementById("loading-overlay").style.display = "block";

  const postPriceInput = document.getElementById("postPrice");
  postPriceInput.value = postPriceInput.value.replace(/[₩,]/g, "");
}

function inputOnPriceEventOn() {
  $(document).on("input", "input#postPrice", function () {
    formatCurrency($(this));
  });
}

function formatNumber(n) {
  // format number 1000000 to 1,234,567
  return n.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function formatCurrency(input, blur) {
  // appends $ to value, validates decimal side
  // and puts cursor back in right position.

  // get input value
  let input_val = input.val();

  // don't validate empty input
  if (input_val === "") {
    return;
  }

  // original length
  let original_len = input_val.length;

  // initial caret position
  let caret_pos = input.prop("selectionStart");

  // check for decimal
  if (input_val.indexOf(".") >= 0) {
    // get position of first decimal
    // this prevents multiple decimals from
    // being entered
    var decimal_pos = input_val.indexOf(".");

    // split number by decimal point
    var left_side = input_val.substring(0, decimal_pos);
    var right_side = input_val.substring(decimal_pos);

    // add commas to left side of number
    left_side = formatNumber(left_side);

    // validate right side
    right_side = formatNumber(right_side);

    // On blur make sure 2 numbers after decimal
    if (blur === "blur") {
      right_side += "00";
    }

    // Limit decimal to only 2 digits
    right_side = right_side.substring(0, 2);

    // join number by .
    input_val = "₩" + left_side + "." + right_side;
  } else {
    // no decimal entered
    // add commas to number
    // remove all non-digits
    input_val = formatNumber(input_val);
    input_val = "₩" + input_val;

    // final formatting
    if (blur === "blur") {
      input_val += ".00";
    }
  }

  // send updated string to input
  input.val(input_val);

  // put caret back in the right position
  let updated_len = input_val.length;
  caret_pos = updated_len - original_len + caret_pos;
  input[0].setSelectionRange(caret_pos, caret_pos);
}
