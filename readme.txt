Programming Assignment 7: Seam Carving
Isaac Schatia

/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */

My code uses a topological shortest paths algorithm. It calculates a shortest paths
energy array of "distTo" energies. Once the array is complete it uses the "edgeTo"
array to trace the shortest path back up to the top of the array which is the seam.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

A picture with a consistent background and a varied focus point makes the code run easily.
For example a picture of someone in a green screen suit would only work well around the face,
assumming it was visible, and work around the body depending on the shadows and if the code
can differentiate it from the background.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

'stadium2000-by-500.png' 0 H
(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
10           2.289               -----       ------
20           4.112               1.796       0.2544
40           8.016               1.949       0.2988
80           15.626              1.949       0.2999
160          27.873              1.784       0.2513

'city500-by-2000.png' W 0
(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
10           0.717               -----       ------
20           1.098               1.531       0.1851
40           1.972               1.796       0.2543
80           3.532               1.791       0.2531
160          6.085               1.723       0.2362
320          9.858               1.620       0.2095

/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */

Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:

    ~  ( ) * W^.23 * H^2.7
       _______________________________________

Estimated the W and H exponent from the log ratios. Unsure how to calculate the
front coefficient.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

Finding the Horizontal seam and removing it takes longer since I coded it by
taking a transpose like the checklist suggested. If I instead re-worked the code
directly It would probably have run as fast as the vertical ones.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

No help.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

Forgot to transpose the picture along with the energy array originally.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */

No partner.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
