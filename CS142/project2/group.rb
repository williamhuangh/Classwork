# adds each_group_by_first_letter to the Enumerable module
# iterates through all letters and yields to the block the words associated
# with a beginning letter
# if the letter has no words that begin with it, the letter is not yielded

module Enumerable
  def each_group_by_first_letter
    letterArray = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']
    letterArray.each do |value|
      returnArr = Array.new
      self.each do |word|
        if word[0] == value
          returnArr.push(word)
        end
      end
      if !returnArr.empty?
        yield(value, returnArr)
      end
    end
  end
end

x = ["abcd", "axyz", "able", "xyzab", "qrst"]
x.each_group_by_first_letter do |letter, words|
  printf("%s: %s\n", letter, words.join(" "))
end

y = ["aweoiqfcd", "dsjkfz", "dwhoifewe", "xyzab", "everyt"]
y.each_group_by_first_letter do |letter, words|
  printf("%s: %s\n", letter, words.join(" "))
end

z = ["weoi", "uxyz", "ube", "ewib", "pqkst"]
z.each_group_by_first_letter do |letter, words|
  printf("%s: %s\n", letter, words.join(" "))
end
