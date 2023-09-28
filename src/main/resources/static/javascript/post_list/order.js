const orderElement = document.querySelector(".order");
     document.addEventListener("DOMContentLoaded", () => {
          orderElement.value = order;
        });

      orderElement.addEventListener("change", function (event) {
          const path = window.location.pathname;
          const url = createUrl({
            page: 1,
            limit,
            order: event.target.value,
            sort,
          });

          window.location.href = url;
        });