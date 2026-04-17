import { ROUTES } from "@/lib/constants/routes";
import { NavLink } from "./Navbar.types";

export const NAV_LINKS: NavLink[] = [
  {
    id: "home",
    href: ROUTES.HOME,
    label: "Home",
  },
  {
    id: "about",
    href: ROUTES.ABOUT,
    label: "About",
  },
];
