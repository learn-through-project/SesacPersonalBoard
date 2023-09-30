import { order, limit, sort } from './init.js';
import { createUrl } from './utils.js';

const sortElement = document.querySelector(".sort");
     document.addEventListener("DOMContentLoaded", () => {
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