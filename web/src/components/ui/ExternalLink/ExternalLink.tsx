import { cn } from "@/lib/utils/cn";
import { ReactNode } from "react";

type ExternalLinkProps = {
  href: string;
  italicize?: boolean;
  children: ReactNode;
};

export const ExternalLink = ({ href, italicize, children }: ExternalLinkProps) => (
  <a className={cn("app-text-link", { italic: italicize })} href={href} target="_blank">
    {children}
  </a>
);
