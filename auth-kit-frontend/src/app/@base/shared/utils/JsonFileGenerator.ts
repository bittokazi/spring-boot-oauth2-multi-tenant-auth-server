export function JsonFileGenerator(blob, filename) {
  //@ts-ignore
  if (window.navigator.msSaveOrOpenBlob)
    //@ts-ignore
    window.navigator.msSaveOrOpenBlob(blob, filename);
  else {
    let a = document.createElement('a'),
      url = URL.createObjectURL(blob);
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    setTimeout(function () {
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    }, 0);
  }
}

export function ExportJson(data, name) {
  console.log(data);
  const c = JSON.stringify(data);
  const file = new Blob([c], { type: 'text/json' });
  JsonFileGenerator(file, name + '.json');
}
