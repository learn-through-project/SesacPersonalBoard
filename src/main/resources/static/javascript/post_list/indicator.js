const PAGE_RANGE = 5;

const getLastPage = (maxPage, currentPage) => {
  if (maxPage < PAGE_RANGE) return maxPage;
  return currentPage > PAGE_RANGE ? currentPage : PAGE_RANGE;
};

const getFivePages = (lastPage) => {
  const pages = [];

  for (let i = 4; i >= 0; i--) {
    pages.push(lastPage - i);
  }

  return pages;
};

const createEachPage = (url, text, isSelected) => {
  const component = document.createElement("a");
  component.setAttribute("href", url);
  component.innerText = text;
  if (isSelected) component.classList.add("isSelected");

  return component;
};

const createUrl = ({ page, limit, order, sort }) => {
  const dummyUrl = "http://www.dummy.com";
  const url = new URL(`${dummyUrl}/post`);
  url.searchParams.append("page", page);
  url.searchParams.append("limit", limit);
  url.searchParams.append("order", order);
  url.searchParams.append("sort", sort);
  return `${url.pathname}${url.search}`;
};

const drawIndicator = (pages, currentPage) => {
  const container = document.querySelector(".indicator");
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
        