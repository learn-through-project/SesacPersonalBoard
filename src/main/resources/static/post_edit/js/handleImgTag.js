
export const handleImageTag = (htmlId) => {
      let cachedId;

      const fileInput = document.getElementById(htmlId);
      const fileIdx = document.getElementById(`${htmlId}_idx`);
      const fileDel = document.getElementById(`${htmlId}_del`);
      const fileThumb = document.getElementById(`${htmlId}_thumb`);

      cachedId = fileIdx.value || -1;

      fileInput.addEventListener("change", (e) => {
         const hasValue = Boolean(event.target.value);
         fileIdx.value = hasValue ? 0 : cachedId;
         fileThumb.hidden = fileIdx.value <= 0;
      });

      fileDel.addEventListener("click", () => {
         cachedId = -1;
         fileIdx.value = -1;
         fileInput.value = null;
         fileThumb.hidden = fileIdx.value <= 0;
      });
};


