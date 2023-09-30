import { order, limit, sort, page, maxPageCount } from './init.js';
import { createUrl } from './utils.js';

const PAGE_RANGE = 5;

const getLastPage = (maxPage, currentPage) => {
  if (maxPage < PAGE_RANGE) return maxPage;
  return currentPage > PAGE_RANGE ? currentPage : PAGE_RANGE;
};

const getFivePages = (lastPage) => {
  const pages = [];

  for (let i = 4; i >= 0; i--) {
    const page = lastPage - i;
    if (page > 0) pages.push(page);
  }

  return pages;
};

const createEachPage = (url, text, isSelected) => {
  const component = document.createElement("a");
  component.setAttribute("href", url);
  component.innerText = text;
  component.style.width = "20px";
  component.style.textAlign = "center";
  if (isSelected) component.classList.add("isSelected");

  return component;
};

const drawIndicator = (pages, currentPage) => {
  const container = document.querySelector(".page-container");
  const tags = pages.map((page) => {
    const url = createUrl({ page, limit, order, sort });
    const component = createEachPage(url, page, page == currentPage);
    return component;
  });

  tags.forEach((tag) => {
    container.appendChild(tag);
  });
};

function handleDomContentLoaded(maxPageCount, page) {
  const lastPage = getLastPage(maxPageCount, page);
  const pages = getFivePages(lastPage);
  drawIndicator(pages, page);
}

// 실행
     document.addEventListener("DOMContentLoaded", (event) => {
          handleDomContentLoaded(maxPageCount, page);
        });

          document.querySelector(".prev").addEventListener("click", () => {
            const prevPage = page - 1;
            if (prevPage > 0) {
              window.location.href = createUrl({page: prevPage, limit, order, sort})
            } else {
              alert("It is the last page");
            }
        });

        document.querySelector(".next").addEventListener("click", () => {
            const nextPage = page + 1;
            if (nextPage <= maxPageCount) {
              window.location.href = createUrl({page: nextPage, limit, order, sort})
            } else {
              alert("It is the last page");
            }
        });
        