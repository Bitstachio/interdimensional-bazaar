import { ComponentProps } from "react";
import { ICONS } from "./registry";

export type IconEntry = {
  label: string;
  svg: React.ElementType;
};

export type IconName = keyof typeof ICONS;
