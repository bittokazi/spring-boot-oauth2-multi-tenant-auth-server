export function JsonImport(event, success, error) {
  let files = event.srcElement.files;
  if (isValidJsonFile(files[0])) {
    let input = event.target;
    console.log(input);

    let reader = new FileReader();
    reader.readAsText(input.files[0]);
    reader.onload = () => {
      success(reader.result);
    };
    reader.onerror = function () {
      error();
    };
  } else {
    error();
  }
}

function isValidJsonFile(file: any) {
  return file.name.endsWith('.json');
}
