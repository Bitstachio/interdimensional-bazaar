import { ICONS } from "./registry";
import { IconName } from "./types";

type IconProps = {
  name: IconName;
  className?: string;
};

export const Icon = ({ name, className }: IconProps) => {
  const Svg = ICONS[name].svg;
  return <Svg className={className} name={name} />;
};
