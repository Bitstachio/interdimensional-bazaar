"use client";

import { cn } from "@/lib/utils/cn";
import { ICONS } from "./registry";
import { IconName } from "./types";

type IconProps = {
  name: IconName;
  className?: string;
};

export const Icon = ({ name, className }: IconProps) => {
  const Svg = ICONS[name].svg;
  return <Svg className={cn("h-5 w-5", className)} />;
};
