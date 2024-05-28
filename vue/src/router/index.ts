import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Manage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: '登录界面',
      component: () => import('@/views/Login.vue')
    },
  ]
})

export const setRoutes = () =>{
  const storeMenus = localStorage.getItem("menus")
  if(storeMenus){
    //拼装动态路由
    const manageRoute = {path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
      ]}

    const menus = JSON.parse(storeMenus)
    menus.forEach((item: { path: string | null | undefined; name: string; children: any[]; pagePath: string }) => {
      if (item.path) {
        const itemMenu = {
          path: item.path.replace("/", ""),
          name: item.name, // 确保 name 是一个字符串类型
          component: () => import(`../views/${item.pagePath}.vue`)
        };
        manageRoute.children.push(itemMenu);
      } else if (item.children.length) {
        item.children.forEach((childItem: { path: string | null | undefined; name: string; pagePath: string }) => {
          if (childItem.path) {
            const itemMenu = {
              path: childItem.path.replace("/", ""),
              name: childItem.name, // 确保 name 是一个字符串类型
              component: () => import(`../views/${childItem.pagePath}.vue`)
            };
            manageRoute.children.push(itemMenu);
          }
        });
      }
    });
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if(!currentRouteNames.includes('Manage')){
      //动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }
  }
}
setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name);

  if (!to.matched.length) {
    const storeMenus = localStorage.getItem("menus");
    if(storeMenus){
      next(); // 菜单数据存在，允许用户访问当前页面
    } else {
      next("/login"); // 菜单数据不存在，跳转到登录页面
    }
  }
  next()
});




export default router
