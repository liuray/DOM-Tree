package structures;

import java.util.*;

import javax.swing.text.html.HTML.Tag;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode,
 * with fields for tag/text, first child and sibling.
 * 
 */
public class Tree {

	/**
	 * Root node
	 */
	TagNode root = null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc
	 *            Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}

	/**
	 * Builds the DOM tree from input HTML file. The root of the tree is stored
	 * in the root field.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		Stack<TagNode> tags = new Stack<TagNode>();
		String curr = sc.nextLine();
		root = new TagNode(curr.substring(1, curr.length() - 1), null, null);

		tags.push(root);
		while (sc.hasNextLine()) {
			curr = sc.nextLine();

			if (curr.charAt(0) == '<') {
				if (curr.charAt(1) != '/') {

					TagNode temp = new TagNode(curr.substring(1,
							curr.length() - 1), null, null);
					if (tags.peek().firstChild == null) {
						tags.peek().firstChild = temp;
						tags.push(temp);
					} else {
						TagNode ptr = tags.peek().firstChild;
						while (ptr.sibling != null) {
							ptr = ptr.sibling;
						}
						ptr.sibling = temp;
						tags.push(temp);
					}
				} else {
					tags.pop();
					continue;
				}
			}

			else {
				TagNode temp = new TagNode(curr, null, null);
				if (tags.peek().firstChild == null) {
					tags.peek().firstChild = temp;
				} else {
					TagNode ptr = tags.peek().firstChild;
					while (ptr.sibling != null) {
						ptr = ptr.sibling;
					}
					ptr.sibling = temp;
				}
			}
		}

	}

	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag
	 *            Old tag
	 * @param newTag
	 *            Replacement tag
	 */
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag
	 *            Old tag
	 * @param newTag
	 *            Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		if ((oldTag.equals("p") || oldTag.equals("b") || oldTag.equals("em"))
				&& (newTag.equals("p") || newTag.equals("b") || newTag
						.equals("em"))) {
			traversal(oldTag, newTag);
		} else if ((oldTag.equals("ol") || oldTag.equals("ul"))
				&& (newTag.equals("ol") || newTag.equals("ul"))) {
			traversal(oldTag, newTag);
		} else
			return;

	}

	// use stack traversal this tree
	private void traversal(String oldTag, String newTag) {
		Stack<TagNode> stack = new Stack<TagNode>();
		stack.push(root);
		while (!stack.isEmpty()) {
			if (stack.peek().tag.equals(oldTag)) {
				stack.peek().tag = newTag;
			}
			TagNode t = stack.pop();
			if (t.sibling != null) {
				stack.push(t.sibling);
			}
			if (t.firstChild != null) {
				stack.push(t.firstChild);
			}
		}

	}

	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The
	 * boldface (b) tag appears directly under the td tag of every column of
	 * this row.
	 * 
	 * @param row
	 *            Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		TagNode table = fingTag(root);
		TagNode tr = table.firstChild;
		int j = 1;
		while (j != row) {
			tr = tr.sibling;
			j++;
		}
		for (TagNode td = tr.firstChild; td != null; td = td.sibling) {
			TagNode b = new TagNode("b", td.firstChild, null);
			td.firstChild = b;
		}

	}

	private TagNode fingTag(TagNode root) {
		if (root == null) {
			return null;
		}
		if (root.tag.equals("table")) {
			return root;
		}
		TagNode sibling = fingTag(root.sibling);

		TagNode first = fingTag(root.firstChild);

		if (sibling != null) {
			return sibling;
		}
		if (first != null) {
			return first;
		}
		return null;
	}

	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/

		if (tag==null || !tag.equals("ol") && !tag.equals("ul") &&! tag.equals("p")
				&&! tag.equals("em") && ! tag.equals("b")) {
			return;//do nothing if tag is not in the list
		}
		Stack<TagNode> stack = new Stack<TagNode>();
		stack.push(root);
		HashMap<TagNode, TagNode> parentMap=new HashMap<TagNode, TagNode>();
		HashMap<TagNode, TagNode> prevSiblingMap=new HashMap<TagNode, TagNode>();
		
		while (!stack.isEmpty()) {
			TagNode t = stack.pop();
			TagNode parent = parentMap.get(t);
			TagNode prevsibling = prevSiblingMap.get(t);
			if(t.tag.equals(tag))
			{
				 if((tag.equals("ol") || tag.equals("ul")) && t.firstChild!=null && t.firstChild.tag.equals("li"))
					{
					   TagNode changeToP = t.firstChild;
					   while(changeToP!=null && changeToP.tag.equals("li"))
					   {
						   changeToP.tag="p";
						   changeToP=changeToP.sibling;
					   }
						
					}
				if(parent!=null )
				{
					if(t.firstChild!=null && t.sibling==null)
					{
						parent.firstChild=t.firstChild;
						
						stack.push(t.firstChild);
						parentMap.put(t.firstChild, parent);
					}
					else
						if(t.firstChild==null && t.sibling!=null)
					{
							parent.firstChild=t.sibling;
							
							stack.push(t.sibling);
							parentMap.put(t.sibling, parent);
						
					}
					else
							if(t.firstChild!=null && t.sibling!=null)
						{
								parent.firstChild=t.firstChild;
								TagNode anode = t.firstChild;
								while(anode.sibling!=null)
									anode=anode.sibling;
								
								anode.sibling=t.sibling;
								
								stack.push(t.firstChild);
								parentMap.put(t.firstChild, parent);
							
						}
					
				}
				else if(prevsibling!=null)
				{
					if(t.firstChild!=null && t.sibling==null)
					{
						prevsibling.sibling=t.firstChild;
						
						stack.push(t.firstChild);
						prevSiblingMap.put(t.firstChild, prevsibling);
					}
					else if(t.firstChild==null && t.sibling!=null)
					{
							prevsibling.sibling=t.sibling;
							
							stack.push(t.sibling);
							prevSiblingMap.put(t.sibling, prevsibling);
						
					}
					else if(t.firstChild!=null && t.sibling!=null)
						{
								prevsibling.sibling=t.firstChild;
								
								stack.push(t.firstChild);
								prevSiblingMap.put(t.firstChild, prevsibling);

								TagNode anode = t.firstChild;
								while(anode.sibling!=null)
									anode=anode.sibling;
								
								anode.sibling=t.sibling;
						}
					
				}
			}else
			{
				if(t.firstChild!=null)
				{
					stack.push(t.firstChild);
					parentMap.put(t.firstChild, t);
				}
				if(t.sibling!=null)
				{
					stack.push(t.sibling);
					prevSiblingMap.put(t.sibling, t);
				}
			}
			
		}

	}

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word
	 *            Word around which tag is to be added
	 * @param tag
	 *            Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		if(!"em".equals(tag) && !"b".equals(tag))
			return; // do nothing if tag is not "em" nor "b"
		Stack<TagNode> stack = new Stack<TagNode>();
		stack.push(root);
		HashMap<TagNode, TagNode> parentMap=new HashMap<TagNode, TagNode>();
		HashMap<TagNode, TagNode> prevSiblingMap=new HashMap<TagNode, TagNode>();
		
		while (!stack.isEmpty()) {
			TagNode t = stack.pop();
			TagNode parent = parentMap.get(t);
			TagNode prevsibling = prevSiblingMap.get(t);
				int index = findTaggableWord(t.tag, word);
				if (index > -1 )// found taggable word, from index position
				{
					if(t.tag.equalsIgnoreCase(word))
					{
							TagNode newTag = new TagNode(tag, t, t.sibling);
							if(parent!=null && !parent.tag.equalsIgnoreCase(tag))
							 parent.firstChild=newTag;
							if(prevsibling!=null && (parent==null  || parent!=null && !parent.tag.equalsIgnoreCase(tag)))
								prevsibling.sibling=newTag;
					}
					else if(index==0)
					{
						int ind=word.length();
						if(isPunctuationChar( t.tag.charAt(ind)))
							ind++;
						TagNode newTag1 = new TagNode(t.tag.substring(0, ind), null, null);
						TagNode newTag2 = new TagNode(t.tag.substring(ind).trim(), null, t.sibling);
						if(newTag2.tag.isEmpty())
							newTag2=t.sibling;
						TagNode newTag3 = new TagNode(tag, newTag1, newTag2);
						if(parent!=null && !parent.tag.equalsIgnoreCase(tag))
							parent.firstChild=newTag3;
						if(prevsibling!=null && (parent==null  || parent!=null && !parent.tag.equalsIgnoreCase(tag)))
							prevsibling.sibling=newTag3;
						if(newTag2!=null)
						{
							stack.push(newTag2);
							prevSiblingMap.put(newTag2, newTag3);
						}

					}
					else
					{
						int ind=index+word.length();
						if(ind < t.tag.length() && isPunctuationChar( t.tag.charAt(ind)))
							ind++;
						TagNode newTag0 = new TagNode(t.tag.substring(0, index).trim(), null, null);
						TagNode newTag1 = new TagNode(t.tag.substring(index, ind), null, null);
						TagNode newTag2 = new TagNode(t.tag.substring(ind).trim(), null, t.sibling);
						if(newTag2.tag.isEmpty())
							newTag2=t.sibling;
						TagNode newTag3 = new TagNode(tag, newTag1, newTag2);
						newTag0.sibling=newTag3;
						if(parent!=null && !parent.tag.equalsIgnoreCase(tag))
							parent.firstChild=newTag0;
						if(prevsibling!=null && (parent==null  || parent!=null && !parent.tag.equalsIgnoreCase(tag)))
							prevsibling.sibling=newTag0;
						if(newTag2!=null)
						{
							stack.push(newTag2);
							prevSiblingMap.put(newTag2, newTag3);
						}
	               }
				}
				if(t.firstChild!=null)
				{
					stack.push(t.firstChild);
					parentMap.put(t.firstChild, t);
				}
				if(t.sibling!=null)
				{
					stack.push(t.sibling);
					prevSiblingMap.put(t.sibling, t);
				}
			
		}

	}
	private boolean isPunctuationChar(char c)
	{
		return c==':' || c=='!'||c=='?'||c==','||c=='.'||c==';';
	}
	//return -1, if no taggable workd found; otherwise, find index for first char
	private int findTaggableWord(String tagText, String word)
	{
		tagText=tagText.toLowerCase();
		word=word.toLowerCase();
		if(word.equals(tagText)) return 0;
		int index=tagText.indexOf(word);
		if(index > -1)
		
		if((index==0 && (tagText.charAt(word.length())==' ' || isPunctuationChar(tagText.charAt(word.length()))))
		     || tagText.charAt(index-1)==' ' 
		                       && ((index+word.length()) ==tagText.length() || tagText.charAt(index+word.length())==' ' || isPunctuationChar(tagText.charAt(index+word.length())) ) ) 
			return index;
		
		return -1;
	}

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the input
	 * file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines.
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr = root; ptr != null; ptr = ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");
			}
		}
	}

}