import type { Metadata } from "next";
import { Inter } from "next/font/google";
import { Navbar } from "@/components/layout/Navbar/Navbar";
import "./globals.css";

const inter = Inter({
  subsets: ["latin"],
  weight: ["400", "500", "600", "700", "800"],
  variable: "--font-sans",
  display: "swap",
});

export const metadata: Metadata = {
  /**
   * e.g. a page exporting title "Cart" renders as "Cart | Bazaar"
   */
  title: {
    template: "%s | Bazaar",
    default: "Bazaar — Rick and Morty Interdimensional Shop",
  },
  description: "A modern platform for online shopping",
};

type RootLayoutProps = {
  children: React.ReactNode;
};

const RootLayout = ({ children }: RootLayoutProps) => (
  <html lang="en" data-theme="dark" className="scroll-smooth">
    <body className={`${inter.variable} antialiased`}>
      <Navbar />
      <main>{children}</main>
    </body>
  </html>
);

export default RootLayout;
