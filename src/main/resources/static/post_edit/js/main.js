import { handleImageTag } from "./handleImgTag.js";

document.addEventListener("DOMContentLoaded", function() {
    console.log('main');
    handleImageTag("file1");
    handleImageTag("file2");
});