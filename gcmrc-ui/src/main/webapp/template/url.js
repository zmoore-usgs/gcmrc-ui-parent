// JavaScript Document
/*This javascript take the Url of the page and writes it to the document every time the page is loaded. The only tricky part
is that if the url is too long, it will split it into two pieces so it doesn't mess up the visual layout of the web site.
example= there are no places for this line to break:
https://www.gcmrc.gov/who_we_are/information_office_program/system_recovery_and_restore/system_recovery_and_restore.aspx
and say our visual layout's width is this:
|-------------------------------------------------------------------------------------------------------|
the url will push the layout to fit the url, the page looks broken. So this javascript splits it into 2 lines if the url
is too long.
*/
lineBR = "";
href = document.URL;
lo = /^(\S+\:\/\/)(.{2,15}\/)?(.{52,100}\/)?(\S+)/;
lo.exec(href);
breakLine = RegExp.$3.split("/");
for (b = 0; b < (breakLine.length - 1); b++) {
if (b == (breakLine.length - 2)) {
lineBR += breakLine[b] + "/<br />";
}
else {
lineBR += breakLine[b] + "/";
}
}
href = RegExp.$1 + RegExp.$2 + lineBR + RegExp.$4;
locate = "The URL of this page is " + href;
document.write(locate);
