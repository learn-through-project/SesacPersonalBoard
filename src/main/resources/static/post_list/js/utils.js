 export const createUrl = ({ page, limit, order, sort }) => {
           const dummyUrl = "http://www.dummy.com";
           const url = new URL(`${dummyUrl}/posts`);
           url.searchParams.append("page", page);
           url.searchParams.append("limit", limit);
           url.searchParams.append("order", order);
           url.searchParams.append("sort", sort);
           return `${url.pathname}${url.search}`;
         };