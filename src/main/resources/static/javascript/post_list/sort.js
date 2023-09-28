const sortElement = document.querySelector(".sort");
     document.addEventListener("DOMContentLoaded", () => {
     console.log('sotrt', sort);
          sortElement.value = sort;
        });

      sortElement.addEventListener("change", function (event) {
          const path = window.location.pathname;
          const url = createUrl({
            page: 1,
            limit,
            order,
            sort: event.target.value,
          });

          window.location.href = url;
        });